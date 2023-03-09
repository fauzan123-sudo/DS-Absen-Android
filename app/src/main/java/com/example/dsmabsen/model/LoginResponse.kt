package com.example.dsmabsen.model

data class LoginResponse(
    val access_token: String,
    val `data`: DataX,
    val message: String,
    val status: Boolean
)