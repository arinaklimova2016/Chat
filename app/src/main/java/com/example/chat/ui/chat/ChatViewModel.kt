package com.example.chat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.MessagesRepository
import com.example.chat.model.MessageDto
import com.example.chat.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatViewModel(
    private val user: User,
    private val map: MessagesRepository
) : ViewModel() {

    private val _messages: MutableLiveData<List<MessageDto>> = MutableLiveData()
    val messages: LiveData<List<MessageDto>> = _messages

    init {
        viewModelScope.launch {
            val get = map.getMessagesByUserId(user.id)
            get.collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            map.sendMessage(user, message)
        }
    }

    fun getYou(): User {
        return map.getYou()
    }

}