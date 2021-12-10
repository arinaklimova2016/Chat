package com.example.domain.model

import com.google.gson.Gson

interface Payload {
    val action: BaseDto.Action
}

private val gson = Gson()

fun Payload.toJson() : String{
    return gson.toJson(
        BaseDto(
            action,
            gson.toJson(this)
        )
    )
}