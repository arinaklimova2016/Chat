package com.example.chat.server

import com.example.chat.constants.Constants.TCP_PORT
import com.example.chat.model.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

class TcpClient {

    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter
    private var gson = Gson()
    lateinit var you: User
    private var timer: Job? = null
    val usersList = MutableSharedFlow<UsersReceivedDto>()
    val newMessage = MutableStateFlow(listOf<MessageDto>())
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun createSocket(ip: String, name: String) {

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
            while (socket.isConnected) {
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
                }
            }
        }
    }

    private fun pong() {
        scope.launch {
            while (socket.isConnected) {
                val baseDto = gson.fromJson(reader.readLine(), BaseDto::class.java)
                when (baseDto.action) {
                    BaseDto.Action.PONG -> {
                        delay(4000)
                        timer?.cancel()
                    }
                    BaseDto.Action.USERS_RECEIVED -> {
                        usersList.emit(gson.fromJson(baseDto.payload, UsersReceivedDto::class.java))
                    }
                    BaseDto.Action.NEW_MESSAGE -> {
                        newMessage.value = newMessage.value + gson.fromJson(baseDto.payload, MessageDto::class.java)
                    }
                    else -> {
                        close()
                    }
                }
            }
        }
    }

    suspend fun getUsers() {
        val getUsers =
            gson.toJson(BaseDto(BaseDto.Action.GET_USERS, gson.toJson(GetUsersDto(you.id))))
        writer.println(getUsers)
        writer.flush()
    }

    suspend fun sendMessage(receiver: String, message: String) {
        val sendMessage = gson.toJson(
            BaseDto(
                BaseDto.Action.SEND_MESSAGE,
                gson.toJson(SendMessageDto(you.id, receiver, message))
            )
        )
        newMessage.value = newMessage.value + MessageDto(you, message)
        writer.println(sendMessage)
        writer.flush()
    }

    private fun close() {
        writer.close()
        reader.close()
        socket.close()
    }

}