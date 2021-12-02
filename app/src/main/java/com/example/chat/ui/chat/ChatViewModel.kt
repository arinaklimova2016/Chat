package com.example.chat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat.model.MessageDto
import com.example.chat.model.User
import com.example.chat.server.TcpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

    fun sendMessage(id: String, message: String){
        tcp.sendMessage(id, message)
    }

    private fun loadMessage(){
        scope.launch {
            tcp.newMessage.collect{
                val currentMessage = _message.value ?: listOf()
                _message.value = currentMessage + it
            }
        }

    }

}