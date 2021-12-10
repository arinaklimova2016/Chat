package com.example.chat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class UsersReceivedDto(val users: List<User>) : Payload {
    override val action: BaseDto.Action
        get() = BaseDto.Action.USERS_RECEIVED
}

@Parcelize
data class User(val id: String, val name: String) : Parcelable