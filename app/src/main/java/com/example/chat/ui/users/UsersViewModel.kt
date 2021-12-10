package com.example.chat.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.data.server.TcpClient
import com.example.chat.model.User
import com.example.chat.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersViewModel(
    private val tcp: TcpClient
) : ViewModel() {

    private val _users: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>> = _users

    private val _errorServer = SingleLiveEvent<Int>()
    val errorServer: LiveData<Int> = _errorServer

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
            val showError = tcp.getError()
            showError.collect {
                withContext(Dispatchers.Main) {
                    _errorServer.value = it
                }
            }

        }
        viewModelScope.launch {
            val usersList = tcp.getUsersList()
            usersList.collect {
                _users.value = it.users
            }
        }
    }
}
