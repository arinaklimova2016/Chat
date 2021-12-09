package com.example.chat.data.server

interface UdpClient {

    suspend fun getServerIp(): String

}