package com.example.data.data.server

interface UdpClient {

    suspend fun getServerIp(): String

}