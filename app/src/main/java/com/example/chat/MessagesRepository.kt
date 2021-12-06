package com.example.chat

import com.example.chat.model.MessageDto
import com.example.chat.model.User
import com.example.chat.server.TcpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MessagesRepository {
    suspend fun getMessagesByUserId(id: String): Flow<List<MessageDto>>
    suspend fun sendMessage(user: User, message: String)
    fun getYou(): User
}

class MessagesRepositoryImpl(private val tcp: TcpClient) : MessagesRepository {

    private val messages = tcp.getNewMessage()

    override suspend fun getMessagesByUserId(id: String): Flow<List<MessageDto>> {
        val you = tcp.getYou()
        return messages.map { it.filter { it.from.id == id || it.from.id == you.id } }
    }

    override suspend fun sendMessage(user: User, message: String) {
        tcp.sendMessage(user.id, message)
    }

    override fun getYou(): User {
        return tcp.getYou()
    }

}