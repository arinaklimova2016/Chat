package com.example.chat.model

data class ConnectedDto(val id: String) : Payload {
    override val action: BaseDto.Action
        get() = BaseDto.Action.CONNECTED
}