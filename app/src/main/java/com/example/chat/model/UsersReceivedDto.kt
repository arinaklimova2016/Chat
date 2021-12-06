package com.example.chat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class UsersReceivedDto(val users: List<User>) : Payload

@Parcelize
data class User(val id: String, val name: String) : Parcelable