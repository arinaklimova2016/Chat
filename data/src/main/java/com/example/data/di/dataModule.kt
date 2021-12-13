package com.example.data.di

import androidx.room.Room
import com.example.data.data.localrepo.LocalRepositoryImpl
import com.example.data.data.room.ChatDatabase
import com.example.data.data.server.TcpClientImpl
import com.example.data.data.server.UdpClient
import com.example.data.data.server.UdpClientImpl
import com.example.domain.repository.LocalRepository
import com.example.domain.repository.TcpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    factory {
        Room.databaseBuilder(
            androidContext(),
            ChatDatabase::class.java,
            ROOM_NAME
        ).build()
    }

    factory {
        get<ChatDatabase>().messageDao()
    }
    single<TcpClient> {
        TcpClientImpl()
    }

    single<UdpClient> {
        UdpClientImpl()
    }

    single<LocalRepository> {
        LocalRepositoryImpl(get())
    }

}

const val ROOM_NAME = "chat_database.db"