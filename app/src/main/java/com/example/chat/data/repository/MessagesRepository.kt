package com.example.chat.data.repository

import com.example.chat.data.room.Message
import com.example.chat.model.User
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun getMessagesByUserId(id: String): Flow<List<Message>>
    suspend fun sendMessage(user: User, message: String)
    fun getYou(): User
    fun getError(): Flow<Int>
}