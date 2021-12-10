package com.example.domain.model

data class PingDto(val id: String) : Payload {
    override val action: BaseDto.Action
        get() = BaseDto.Action.PING
}