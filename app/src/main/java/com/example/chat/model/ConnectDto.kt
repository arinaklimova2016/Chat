package com.example.chat.model

data class ConnectDto(val id: String, val name: String) : Payload {
    override val action: BaseDto.Action
        get() = BaseDto.Action.CONNECT
}