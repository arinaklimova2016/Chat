package com.example.chat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.model.MessageDto
import com.example.chat.model.User
import com.example.chat.server.TcpClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class ChatViewModel(
    private val tcp: TcpClient
) : ViewModel() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private val _message: MutableLiveData<List<MessageDto>> = MutableLiveData()
    val message: LiveData<List<MessageDto>> = _message

    init {
        loadMessage()
    }

    fun sendMessage(user: User, message: String){
        viewModelScope.launch(Dispatchers.IO) {
            tcp.sendMessage(user.id, message)
            withContext(Dispatchers.Main){
                val currentMessage = _message.value ?: listOf()
                _message.value = currentMessage + MessageDto(from = getYou(), message = message)
            }
        }
    }

    private fun loadMessage(){
        scope.launch {
            tcp.newMessage.collect{
                val currentMessage = _message.value ?: listOf()
                _message.value = currentMessage + it
            }
        }
    }

    fun getYou(): User {
        return tcp.you
    }

}