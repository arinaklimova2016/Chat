package com.example.domain.repository.generate

internal class IdGeneratorImpl : IdGenerator {

    override fun generateId(length: Int): String {
        return buildString {
            repeat(length) {
                append(ID_CHARS.random())
            }
        }
    }

    companion object {
        private const val ID_CHARS = "qwertyuiopasdfghjklzxcvbnm1234567890"
    }
}