package com.example.chat.data.server

import com.example.chat.model.*
import com.example.chat.utils.Constants.DELAY
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

class TcpClientImpl : TcpClient {

    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter

    private var gson = Gson()
    private var timer: Job? = null

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val usersList = MutableSharedFlow<UsersReceivedDto>()
    private val newMessage = MutableStateFlow<MessageDto?>(null)
    private val userName = MutableStateFlow<String?>(null)
    private val userId = MutableStateFlow<ConnectedDto?>(null)
    private val you = MutableStateFlow<User?>(null)
    private val showError = MutableSharedFlow<Int>()

    companion object {
        const val TCP_PORT = 6666
        const val TIMEOUT = 15000
        const val ERROR404 = 404
    }

    override suspend fun createSocket(ip: String, name: String) {
        socket = Socket(ip, TCP_PORT)
        socket.soTimeout = TIMEOUT
        reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        writer = PrintWriter(OutputStreamWriter(socket.getOutputStream()))
        userName.emit(name)
        inspector()
        gson = Gson()
    }

    private fun ping() {
        scope.launch {
            while (!socket.isClosed) {
                delay(DELAY)
                try {
                    writer.println(
                        gson.toJson(
                            BaseDto(
                                BaseDto.Action.PING,
                                gson.toJson(PingDto(you.value!!.id))
                            )
                        )
                    )
                    writer.flush()
                    timer = scope.launch {
                        delay(DELAY)
                        close()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    close()
                }
            }
        }
    }

    private fun inspector() {
        scope.launch {
            while (!socket.isClosed) {
                try {
                    val baseDto = gson.fromJson(reader.readLine(), BaseDto::class.java)
                    when (baseDto.action) {
                        BaseDto.Action.CONNECTED -> {
                            userId.value = gson.fromJson(baseDto.payload, ConnectedDto::class.java)
                            confirmConnect()
                        }
                        BaseDto.Action.PONG -> {
                            timer?.cancel()
                        }
                        BaseDto.Action.USERS_RECEIVED -> {
                            usersList.emit(
                                gson.fromJson(
                                    baseDto.payload,
                                    UsersReceivedDto::class.java
                                )
                            )
                        }
                        BaseDto.Action.NEW_MESSAGE -> {
                            newMessage.value = gson.fromJson(
                                baseDto.payload,
                                MessageDto::class.java
                            )
                        }
                        else -> {
                            close()
                        }
                    }
                } catch (e: java.lang.Exception) {
                    showError.emit(ERROR404)
                    e.printStackTrace()
                    close()
                }
            }
        }
    }

    private suspend fun confirmConnect() {
        val id: String = userId.first()?.id ?: ""
        val name = userName.first() ?: ""
        you.emit(User(id, name))
        writer.println(
            gson.toJson(
                BaseDto(
                    BaseDto.Action.CONNECT,
                    gson.toJson(ConnectDto(userId.value!!.id, userName.value!!))
                )
            )
        )
        writer.flush()
        ping()
    }

    override suspend fun getUsers() {
        val userId = you.first()?.id ?: ""
        val getUsers = gson.toJson(
            BaseDto(
                BaseDto.Action.GET_USERS,
                gson.toJson(
                    GetUsersDto(userId)
                )
            )
        )
        writer.println(getUsers)
        writer.flush()
    }

    override fun getUsersList(): Flow<UsersReceivedDto> {
        return usersList
    }

    override suspend fun sendMessage(receiver: String, message: String) {
        val sendMessage = gson.toJson(
            BaseDto(
                BaseDto.Action.SEND_MESSAGE,
                gson.toJson(
                    SendMessageDto(
                        you.value!!.id,
                        receiver,
                        message
                    )
                )
            )
        )
        writer.println(sendMessage)
        writer.flush()
    }

    override fun getNewMessage(): Flow<MessageDto> {
        return newMessage.filterNotNull()
    }

    override fun getError(): Flow<Int> {
        return showError
    }

    override fun getYou(): User {
        return you.value!!
    }

    private fun close() {
        writer.close()
        reader.close()
        socket.close()
        job.cancelChildren()
    }

}