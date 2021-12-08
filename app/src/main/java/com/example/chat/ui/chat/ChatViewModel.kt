package com.example.chat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.MessagesRepository
import com.example.chat.model.User
import com.example.chat.room.Message
import com.example.chat.singleliveevent.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val user: User,
    //переименовать
    private val map: MessagesRepository
) : ViewModel() {

    private val _messages: MutableLiveData<List<Message>> = MutableLiveData()
    val messages: LiveData<List<Message>> = _messages

    //спрятать в лайв дата
    val errorServer = SingleLiveEvent<Int>()

    init {
        viewModelScope.launch {
            map.getMessagesByUserId(user.id).collect {
                _messages.value = it
            }
        }
        viewModelScope.launch {
            map.getError().collect {
                withContext(Dispatchers.Main) {
                    errorServer.value = it
                }
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