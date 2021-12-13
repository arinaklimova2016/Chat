package com.example.chat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.model.UiUser
import com.example.chat.model.toUi
import com.example.chat.model.toUser
import com.example.chat.utils.SingleLiveEvent
import com.example.data.data.room.Message
import com.example.data.data.room.toDatabase
import com.example.domain.repository.MessagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val user: UiUser,
    private val repository: MessagesRepository
) : ViewModel() {

    private val _messages: MutableLiveData<List<Message>> = MutableLiveData()
    val messages: LiveData<List<Message>> = _messages

    private val _errorServer = SingleLiveEvent<Int>()
    val errorServer: LiveData<Int> = _errorServer

    init {
        viewModelScope.launch {
            repository.getMessagesByUserId(user.id).collect {
                _messages.value = it.map {
                    it.toDatabase()
                }
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
            repository.sendMessage(user.toUser(), message)
        }
    }

    fun getYou(): UiUser {
        return repository.getYou().toUi()
    }

}