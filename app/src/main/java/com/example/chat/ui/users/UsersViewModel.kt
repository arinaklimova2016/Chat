package com.example.chat.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.model.User
import com.example.chat.server.TcpClient
import com.example.chat.server.TcpClientImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
            while (true) {
                delay(1000)
                tcp.getUsers()
            }
        }
    }

    private fun observer() {
        viewModelScope.launch {
            val usersList = tcp.getUsersList()
            usersList.collect {
                _users.value = it.users
            }
        }
    }
}
