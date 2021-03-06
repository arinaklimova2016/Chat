package com.example.domain.model

data class MessageDto(val from: User, val message: String) : Payload {
    override val action: BaseDto.Action
        get() = BaseDto.Action.NEW_MESSAGE
}