package com.example.chat.ui.login

import androidx.lifecycle.LiveData
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

    private val _triggerNextNavigation = SingleLiveEvent<Unit>()
    val triggerNextNavigation: LiveData<Unit> = _triggerNextNavigation

    private val _errorServer = SingleLiveEvent<Unit>()
    val errorServer: LiveData<Unit> = _errorServer

    fun getIp(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ip = udp.getServerIp()
                tcp.createSocket(ip, name)
                withContext(Dispatchers.Main) {
                    _triggerNextNavigation.call()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _errorServer.call()
                }
            }
        }
    }
}