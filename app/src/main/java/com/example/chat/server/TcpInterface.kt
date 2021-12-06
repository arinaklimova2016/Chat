package com.example.chat.server

import com.example.chat.model.MessageDto
import com.example.chat.model.User
import com.example.chat.model.UsersReceivedDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

interface TcpInterface {

    fun createSocket(ip: String, name: String)
    suspend fun getUsers()
    fun getUsersList(): Flow<UsersReceivedDto>
    suspend fun sendMessage(receiver: String, message: String)
    fun getNewMessage(): Flow<List<MessageDto>>
    fun getYou(): User

}