package com.example.domain.repository

import com.example.domain.model.DomainMessage
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun getMessagesByUserId(id: String): Flow<List<DomainMessage>>
    suspend fun sendMessage(user: User, message: String)
    fun getYou(): User
    fun getError(): Flow<Int>
}