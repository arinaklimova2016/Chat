package com.example.domain.repository.generate

interface IdGenerator {

    fun generateId(length: Int = 32): String

}