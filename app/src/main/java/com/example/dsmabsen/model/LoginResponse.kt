package com.example.dsmabsen.model

data class LoginResponse(
    val access_token: String,
    val message: String,
    val status: Boolean,
    val user: User
)