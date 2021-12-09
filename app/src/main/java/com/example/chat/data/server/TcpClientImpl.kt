package com.example.chat.data.server

import com.example.chat.model.*
import com.example.chat.utils.Constants.DELAY
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

class TcpClientImpl : TcpClient {

    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter
    private lateinit var you: User
    private lateinit var userId: ConnectedDto
    private var gson = Gson()
    private var timer: Job? = null
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val usersList = MutableSharedFlow<UsersReceivedDto>()
    private val newMessage = MutableStateFlow<MessageDto?>(null)
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
        inspector()
        gson = Gson()

        you = User(userId.id, name)
        writer.println(
            gson.toJson(
                BaseDto(
                    BaseDto.Action.CONNECT,
                    gson.toJson(ConnectDto(userId.id, name))
                )
            )
        )
        writer.flush()
        ping()
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
                                gson.toJson(PingDto(you.id))
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
                            userId = gson.fromJson(baseDto.payload, ConnectedDto::class.java)
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

    override suspend fun getUsers() {
        writer.println(
            gson.toJson(
                BaseDto(
                    BaseDto.Action.GET_USERS,
                    gson.toJson(GetUsersDto(you.id))
                )
            )
        )
        writer.flush()
    }

    override fun getUsersList(): Flow<UsersReceivedDto> {
        return usersList
    }

    override suspend fun sendMessage(receiver: String, message: String) {
        writer.println(
            gson.toJson(
                BaseDto(
                    BaseDto.Action.SEND_MESSAGE,
                    gson.toJson(SendMessageDto(you.id, receiver, message))
                )
            )
        )
        writer.flush()
    }

    override fun getNewMessage(): Flow<MessageDto> {
        return newMessage.filterNotNull()
    }

    override fun getError(): Flow<Int> {
        return showError
    }

    override fun getYou(): User {
        return you
    }

    private fun close() {
        writer.close()
        reader.close()
        socket.close()
        job.cancelChildren()
    }

}