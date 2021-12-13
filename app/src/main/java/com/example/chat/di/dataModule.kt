package com.example.chat.di

import androidx.room.Room
import com.example.data.data.localrepo.LocalRepositoryImpl
import com.example.data.data.room.ChatDatabase
import com.example.data.data.server.TcpClientImpl
import com.example.data.data.server.UdpClient
import com.example.data.data.server.UdpClientImpl
import com.example.domain.repository.LocalRepository
import com.example.domain.repository.MessagesRepository
import com.example.domain.repository.TcpClient
import com.example.domain.repository.generate.IdGenerator
import com.example.domain.repository.generate.IdGeneratorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<MessagesRepository> {
        com.example.domain.repository.MessagesRepositoryImpl(
            tcp = get(),
            localRepository = get(),
            idGenerator = get()
        )
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

    single<LocalRepository> {
        LocalRepositoryImpl(get())
    }

    factory {
        get<ChatDatabase>().messageDao()
    }

    factory<IdGenerator> {
        IdGeneratorImpl()
    }
}

const val ROOM_NAME = "chat_database.db"