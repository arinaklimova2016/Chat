package com.example.chat.server

import com.example.chat.constants.Constants.TCP_PORT
import com.example.chat.model.*
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
    private var gson = Gson()
    private var timer: Job? = null
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var you: User
    private val usersList = MutableSharedFlow<UsersReceivedDto>()
    private val newMessage = MutableStateFlow<MessageDto?>(null)
    private val showError = MutableSharedFlow<Int>()

    override fun createSocket(ip: String, name: String) {
        socket = Socket(ip, TCP_PORT)
        socket.soTimeout = 15000
        reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        writer = PrintWriter(OutputStreamWriter(socket.getOutputStream()))
        gson = Gson()
        val baseDto = gson.fromJson(reader.readLine(), BaseDto::class.java)
        val connectedDto = gson.fromJson(baseDto?.payload, ConnectedDto::class.java)
        val connectDto = gson.toJson(ConnectDto(connectedDto.id, name))

        you = User(connectedDto.id, name)

        val baseDtoJson = gson.toJson(BaseDto(BaseDto.Action.CONNECT, connectDto))

        //send
        writer.println(baseDtoJson)
        writer.flush()
        ping()
    }

    private fun ping() {
        scope.launch {
            while (!socket.isClosed) {
                delay(5000)
                val pingDto =
                    gson.toJson(BaseDto(BaseDto.Action.PING, gson.toJson(PingDto(you.id))))
                try {
                    writer.println(pingDto)
                    writer.flush()
                    timer = scope.launch {
                        delay(5000)
                        close()
                    }
                    pong()
                } catch (e: Exception) {
                    e.printStackTrace()
                    close()
                }
            }
        }
    }

    private fun pong() {
        scope.launch {
            while (!socket.isClosed) {
                try {
                    val baseDto = gson.fromJson(reader.readLine(), BaseDto::class.java)
                    when (baseDto.action) {
                        BaseDto.Action.PONG -> {
                            timer?.cancel()
                        }
                        BaseDto.Action.USERS_RECEIVED -> {
                            usersList.emit(gson.fromJson(baseDto.payload, UsersReceivedDto::class.java))
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
                } catch (e:java.lang.Exception){
                    showError.emit(404)
                    e.printStackTrace()
                    close()
                }
            }
        }
    }

    override suspend fun getUsers() {
        delay(1000)
        val getUsers =
            gson.toJson(BaseDto(BaseDto.Action.GET_USERS, gson.toJson(GetUsersDto(you.id))))
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
                gson.toJson(SendMessageDto(you.id, receiver, message))
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
        return you
    }

    private fun close() {
        writer.close()
        reader.close()
        socket.close()
        job.cancelChildren()
    }

}