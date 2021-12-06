package com.example.chat

import com.example.chat.model.MessageDto
import com.example.chat.model.User
import com.example.chat.server.TcpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface MessagesRepository {
    suspend fun getMessagesByUserId(id: String): Flow<List<MessageDto>>
    suspend fun sendMessage(user: User, message: String)
}

class MessagesRepositoryImpl(private val tcp: TcpClient) : MessagesRepository {

    private val messages = tcp.newMessage

    override suspend fun getMessagesByUserId(id: String): Flow<List<MessageDto>> {
        val you = tcp.you
        return messages.map { it.filter { it.from.id == id || it.from.id == you.id } }
    }

    override suspend fun sendMessage(user: User, message: String) {
        tcp.sendMessage(user.id, message)
    }

}