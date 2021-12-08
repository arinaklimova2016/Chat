package com.example.chat.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.server.TcpClient
import com.example.chat.server.UdpClient
import com.example.chat.singleliveevent.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val udp: UdpClient,
    private val tcp: TcpClient
) : ViewModel() {

    //переименовать
    val idSingleLiveEvent = SingleLiveEvent<Unit>()
    val errorSingleLiveEvent = SingleLiveEvent<Unit>()

    fun getIp(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ip = udp.getServerIp()
                tcp.createSocket(ip, name)
                withContext(Dispatchers.Main) {
                    idSingleLiveEvent.call()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    errorSingleLiveEvent.call()
                }
            }
        }
    }
}