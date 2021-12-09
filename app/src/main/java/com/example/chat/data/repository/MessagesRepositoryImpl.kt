package com.example.chat.data.repository

import com.example.chat.data.room.Message
import com.example.chat.data.room.MessageDao
import com.example.chat.data.server.TcpClient
import com.example.chat.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MessagesRepositoryImpl(
    private val tcp: TcpClient,
    private val messageDao: MessageDao
) : MessagesRepository {

    private val message = tcp.getNewMessage()
    private val you = tcp.getYou()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val errorListener = MutableSharedFlow<Int>()

    init {
        scope.launch(Dispatchers.IO) {
            messageDao.deleteAllChats()
            message.collect {
                messageDao.addMessage(
                    message = Message(
                        from = it.from.id,
                        to = you.id,
                        message = it.message
                    )
                )
            }
        }
        scope.launch {
            val getError = tcp.getError()
            getError.collect {
                errorListener.emit(it)
            }
        }
    }

    override suspend fun getMessagesByUserId(id: String): Flow<List<Message>> {
        return messageDao.getById(id)
    }

    override suspend fun sendMessage(user: User, message: String) {
        tcp.sendMessage(user.id, message)
        val newMessage = Message(from = you.id, to = user.id, message = message)
        messageDao.addMessage(newMessage)
    }

    override fun getYou(): User {
        return you
    }

    override fun getError(): Flow<Int> {
        return errorListener
    }

}