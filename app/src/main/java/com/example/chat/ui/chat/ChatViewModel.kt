package com.example.chat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.data.repository.MessagesRepository
import com.example.chat.data.room.Message
import com.example.chat.model.User
import com.example.chat.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val user: User,
    private val repository: MessagesRepository
) : ViewModel() {

    private val _messages: MutableLiveData<List<Message>> = MutableLiveData()
    val messages: LiveData<List<Message>> = _messages

    private val _errorServer = SingleLiveEvent<Int>()
    val errorServer: LiveData<Int> = _errorServer

    init {
        viewModelScope.launch {
            repository.getMessagesByUserId(user.id).collect {
                _messages.value = it
            }
        }
        viewModelScope.launch {
            repository.getError().collect {
                withContext(Dispatchers.Main) {
                    _errorServer.value = it
                }
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMessage(user, message)
        }
    }

    fun getYou(): User {
        return repository.getYou()
    }

}