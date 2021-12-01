package com.example.chat.server

import com.example.chat.constants.Constants.TCP_PORT
import com.example.chat.model.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket

class TcpClient {

    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter
    private lateinit var gson: Gson
    private lateinit var id: String
    private var timer: Job? = null
    lateinit var usersList: UsersReceivedDto
    lateinit var newMessage: MessageDto

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun createSocket(ip: String, name: String) {

        socket = Socket(ip, TCP_PORT)
        socket.soTimeout = 15000
        reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        writer = PrintWriter(OutputStreamWriter(socket.getOutputStream()))
        gson = Gson()
        val baseDto = gson.fromJson(reader.readLine(), BaseDto::class.java)
        val connectedDto = gson.fromJson(baseDto.payload, ConnectedDto::class.java)
        val connectDto = gson.toJson(ConnectDto(connectedDto.id, name))

        id = connectedDto.id

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
                val pingDto = gson.toJson(PingDto(id))
                val pingDtoJson = gson.toJson(BaseDto(BaseDto.Action.PING, pingDto))
                try {
                    writer.println(pingDtoJson)
                    writer.flush()
                    timer = scope.launch {
                        delay(5000)
                        socket.close()
                    }
                    pong()
                }catch (e:Exception){e.printStackTrace()}

            }
        }

    }

    private fun pong() {
        scope.launch {
            val baseDto = gson.fromJson(reader.readLine(), BaseDto::class.java)
            when (baseDto.action) {
                BaseDto.Action.PONG -> {
                    delay(4000)
                    timer?.cancel()

                }
                BaseDto.Action.USERS_RECEIVED -> {
                    usersList = gson.fromJson(baseDto.payload, UsersReceivedDto::class.java)
                }
                BaseDto.Action.NEW_MESSAGE -> {
                    newMessage = gson.fromJson(baseDto.payload, MessageDto::class.java)
                }
                else -> {
                    socket.close()
                }
            }
        }
    }

}