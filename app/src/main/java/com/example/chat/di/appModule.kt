package com.example.chat.di

import com.example.chat.server.TcpClient
import com.example.chat.server.TcpClientImpl
import com.example.chat.server.UdpClient
import com.example.chat.ui.chat.ChatViewModel
import com.example.chat.ui.login.LoginViewModel
import com.example.chat.ui.users.UsersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        LoginViewModel(
            udp = get(),
            tcp = get()
        )
    }

    viewModel {
        UsersViewModel(
            tcp = get()
        )
    }

    viewModel { parameters ->
        ChatViewModel(
            user = parameters[0],
            map = get()
        )
    }

    factory {
        provideUdpClient()
    }

    single<TcpClient> {
        TcpClientImpl()
    }
}

private fun provideUdpClient(): UdpClient {
    return UdpClient()
}
