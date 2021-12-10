package com.example.chat.model

data class SendMessageDto(val id: String, val receiver: String, val message: String) : Payload {
    override val action: BaseDto.Action
        get() = BaseDto.Action.SEND_MESSAGE
}