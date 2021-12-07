package com.example.chat.di

import androidx.room.Room
import com.example.chat.MessagesRepository
import com.example.chat.MessagesRepositoryImpl
import com.example.chat.constants.Constants
import com.example.chat.room.ChatDatabase
import com.example.chat.room.MessageDao
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
            Constants.ROOM_NAME
        ).build()
    }

    factory {
        provideMessageDao(get())
    }
}

private fun provideMessageDao(database: ChatDatabase): MessageDao {
    return database.messageDao()
}