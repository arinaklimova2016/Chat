package com.example.chat.di

import com.example.chat.chat.ChatViewModel
import com.example.chat.login.LoginViewModel
import com.example.chat.server.TcpClient
import com.example.chat.server.UdpClient
import com.example.chat.users.UsersViewModel
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

    viewModel {
        ChatViewModel(
            tcp = get()
        )
    }

    factory {
        provideUdpClient()
    }

    factory {
        provideTcpClient()
    }

}

fun provideTcpClient(): TcpClient {
    return TcpClient()
}

private fun provideUdpClient():UdpClient{
    return UdpClient()
}
