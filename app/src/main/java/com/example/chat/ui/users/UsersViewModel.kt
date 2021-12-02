package com.example.chat.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat.model.User
import com.example.chat.server.TcpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UsersViewModel(
    private val tcp: TcpClient
) : ViewModel() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private val _users: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>> = _users

    init {
        observer()
        getUsers()
    }

    fun getUsers() {
        tcp.getUsers()
    }

    private fun observer() {
        scope.launch {
            tcp.usersList.collect {
                _users.value = it.users
            }
        }
    }


}