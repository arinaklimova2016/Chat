package com.example.chat.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.data.server.TcpClient
import com.example.chat.data.server.UdpClient
import com.example.chat.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val udp: UdpClient,
    private val tcp: TcpClient
) : ViewModel() {

    val listenerId = SingleLiveEvent<Unit>()
    val listenerError = SingleLiveEvent<Unit>()

    fun getIp(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ip = udp.getServerIp()
                tcp.createSocket(ip, name)
                withContext(Dispatchers.Main) {
                    listenerId.call()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    listenerError.call()
                }
            }
        }
    }
}