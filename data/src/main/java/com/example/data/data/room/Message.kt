package com.example.data.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import com.example.domain.model.DomainMessage

@Entity
data class Message(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val from: String,
    val to: String,
    val message: String
)

fun Message.toDatabase(): DomainMessage {
    return DomainMessage(
        from = from,
        to = to,
        message = message
    )
}