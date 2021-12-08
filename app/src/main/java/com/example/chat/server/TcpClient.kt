package com.example.chat.server

import com.example.chat.model.MessageDto
import com.example.chat.model.User
import com.example.chat.model.UsersReceivedDto
import kotlinx.coroutines.flow.Flow

interface TcpClient {
    fun createSocket(ip: String, name: String)
    suspend fun getUsers()
    fun getUsersList(): Flow<UsersReceivedDto>
    suspend fun sendMessage(receiver: String, message: String)
    fun getNewMessage(): Flow<MessageDto>
    fun getError(): Flow<Int>
    fun getYou(): User
}