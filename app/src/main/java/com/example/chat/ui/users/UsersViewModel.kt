package com.example.chat.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.model.User
import com.example.chat.server.TcpClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class UsersViewModel(
    private val tcp: TcpClient
) : ViewModel() {

    private val _users: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>> = _users

    init {
        observer()
        getUsers()
    }

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true){
                delay(2000)
                tcp.getUsers()
            }
        }
    }

    private fun observer() {
        viewModelScope.launch {
            tcp.usersList.collect {
                _users.value = it.users
            }
        }
    }
}
