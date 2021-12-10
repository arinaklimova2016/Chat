package com.example.domain.repository

import com.example.domain.model.DomainMessage
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    fun getMessageById(id: String): Flow<List<DomainMessage>>
    fun addMessage(message: DomainMessage)
    fun deleteAllChats()
}