package com.example.chat.users

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

    lateinit var users: List<User>

    init {
        getUsers()
        observer()
    }

    fun getUsers() {
        tcp.getUsers()
    }

    private fun observer() {
        scope.launch {
            tcp.usersList.collect {
                users = it.users
            }
        }
    }


}