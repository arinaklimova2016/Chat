package com.example.chat.chat

import androidx.lifecycle.ViewModel
import com.example.chat.server.TcpClient

class ChatViewModel(
    private val tcp: TcpClient
) : ViewModel() {
}