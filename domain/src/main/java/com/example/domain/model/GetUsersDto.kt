package com.example.domain.model

data class GetUsersDto(val id: String) : Payload {
    override val action: BaseDto.Action
        get() = BaseDto.Action.GET_USERS
}