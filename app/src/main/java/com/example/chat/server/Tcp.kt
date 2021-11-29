package com.example.chat.server

import com.example.chat.constants.Constants.TCP_PORT
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

class Tcp {

    private val port = TCP_PORT

    fun createSocket(ip: String){

        val socket = Socket(ip, port)

        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val writer = PrintWriter(OutputStreamWriter(socket.getOutputStream()))

        val getMessage = reader.readLine()
        val sendMessage = writer.println("Hello")
        writer.flush()

    }


}