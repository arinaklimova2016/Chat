package com.example.domain.repository

import com.example.domain.model.DomainMessage
import com.example.domain.model.User
import com.example.domain.repository.generate.IdGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class MessagesRepositoryImpl(
    private val tcp: TcpClient,
    private val localRepository: LocalRepository,
    private val idGenerator: IdGenerator
) : MessagesRepository {

    private val message = tcp.getNewMessage()
    private val you = tcp.getYou()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val errorListener = MutableSharedFlow<Int>()

    init {
        scope.launch(Dispatchers.IO) {
            localRepository.deleteAllChats()
            message.collect {
                localRepository.addMessage(
                    message = DomainMessage(
                        id = idGenerator.generateId(),
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

    override suspend fun getMessagesByUserId(id: String): Flow<List<DomainMessage>> {
        return localRepository.getMessageById(id)
    }

    override suspend fun sendMessage(user: User, message: String) {
        tcp.sendMessage(user.id, message)
        val newMessage = DomainMessage(
            id = idGenerator.generateId(),
            from = you.id,
            to = user.id,
            message = message
        )
        localRepository.addMessage(newMessage)
    }

    override fun getYou(): User {
        return you
    }

    override fun getError(): Flow<Int> {
        return errorListener
    }
}