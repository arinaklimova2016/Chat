package com.example.domain.model

data class UsersReceivedDto(val users: List<User>) : Payload {
    override val action: BaseDto.Action
        get() = BaseDto.Action.USERS_RECEIVED
}

data class User(val id: String, val name: String)