package com.example.dsmabsen.repository

import com.example.dsmabsen.network.AuthApi
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: AuthApi) : BaseRepository() {

    suspend fun loginUser(email: String, password: String, imei: String) =
        safeApiCall { api.loginUser(email, password, imei) }
}