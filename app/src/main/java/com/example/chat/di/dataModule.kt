package com.example.chat.di

import androidx.room.Room
import com.example.chat.data.repository.MessagesRepository
import com.example.chat.data.repository.MessagesRepositoryImpl
import com.example.chat.data.room.ChatDatabase
import com.example.chat.data.server.TcpClient
import com.example.chat.data.server.TcpClientImpl
import com.example.chat.data.server.UdpClient
import com.example.chat.data.server.UdpClientImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<MessagesRepository> {
        MessagesRepositoryImpl(tcp = get(), messageDao = get())
    }

    factory {
        Room.databaseBuilder(
            androidContext(),
            ChatDatabase::class.java,
            ROOM_NAME
        ).build()
    }

    single<TcpClient> {
        TcpClientImpl()
    }

    single<UdpClient> {
        UdpClientImpl()
    }

    factory {
        get<ChatDatabase>().messageDao()
    }
}

const val ROOM_NAME = "chat_database.db"