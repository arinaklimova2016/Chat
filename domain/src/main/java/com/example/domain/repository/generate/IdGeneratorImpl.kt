package com.example.domain.repository.generate

class IdGeneratorImpl : IdGenerator {

    private val idChars = ID_CHARS

    override fun generateId(length: Int): String {
        return buildString {
            repeat(length) {
                append(idChars.random())
            }
        }
    }

    companion object {
        private const val ID_CHARS = "qwertyuiopasdfghjklzxcvbnm1234567890"
    }
}