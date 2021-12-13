package com.example.data.data.localrepo

import com.example.data.data.room.Message
import com.example.data.data.room.MessageDao
import com.example.data.data.room.toDatabase
import com.example.data.data.room.toDomain
import com.example.domain.model.DomainMessage
import com.example.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalRepositoryImpl(
    private val messageDao: MessageDao
): LocalRepository {
    override fun getMessageById(id: String): Flow<List<DomainMessage>> {
        return messageDao.getById(id).map {
            it.map {
                it.toDomain()
            }
        }
    }

    override fun addMessage(message: DomainMessage) {
        val message = message.toDatabase()
        messageDao.addMessage(message)
    }

    override fun deleteAllChats() {
        messageDao.deleteAllChats()
    }

}