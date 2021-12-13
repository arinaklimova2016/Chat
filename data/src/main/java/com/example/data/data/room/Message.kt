package com.example.data.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.DomainMessage

@Entity
data class Message(
    @PrimaryKey
    val id: String,
    val from: String,
    val to: String,
    val message: String
)

fun DomainMessage.toDatabase(): Message {
    return Message(
        id = id,
        from = from,
        to = to,
        message = message
    )
}

fun Message.toDomain(): DomainMessage {
    return DomainMessage(
        id = id,
        from = from,
        to = to,
        message = message
    )
}