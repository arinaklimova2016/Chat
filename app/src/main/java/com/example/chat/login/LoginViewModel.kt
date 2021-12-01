package com.example.chat.login

import androidx.lifecycle.ViewModel
import com.example.chat.server.TcpClient
import com.example.chat.server.UdpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LoginViewModel(
    private val udp: UdpClient,
    private val tcp: TcpClient
) : ViewModel() {

    private val job by lazy { SupervisorJob() }
    private val scope by lazy {
        CoroutineScope(job + Dispatchers.IO)
    }

    fun getIp(name: String) {
        scope.launch{
            val ip = udp.getServerIp()
            tcp.createSocket(ip, name)
        }
    }

}