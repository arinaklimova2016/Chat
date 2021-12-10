package com.example.chat.model

data class PongDto(val id: String) : Payload {
    override val action: BaseDto.Action
        get() = BaseDto.Action.PONG
}