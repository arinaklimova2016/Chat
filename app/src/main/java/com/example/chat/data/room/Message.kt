package com.example.chat.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Message(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val from: String,
    val to: String,
    val message: String
)