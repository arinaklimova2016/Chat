package com.example.chat.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Message::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}