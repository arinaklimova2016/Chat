package com.example.chat.model

import android.os.Parcelable
import com.example.domain.model.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiUser(val id:String, val name: String): Parcelable

fun UiUser.toUser(): User{
    return User(
        id = id,
        name = name
    )
}

fun User.toUi(): UiUser{
    return UiUser(
        id = id,
        name = name
    )
}