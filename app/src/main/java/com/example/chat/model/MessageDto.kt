package com.example.chat.model

data class MessageDto(val from: User, val message: String) : Payload