package com.example.domain.model

data class DomainMessage(
    val id: String,
    val from: String,
    val to: String,
    val message: String
)