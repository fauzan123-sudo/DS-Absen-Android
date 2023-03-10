package com.example.dsmabsen.repository

import com.example.dsmabsen.network.AuthApi
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: AuthApi) : BaseRepository() {

    suspend fun loginUser(email: String, password: String, imei: String) =
        safeApiCall { api.loginUser(email, password, imei) }

    suspend fun logOut(nip: String) =
        safeApiCall { api.logOut(nip) }

    suspend fun logOuts(nip: String) =
        api.logOuts(nip)

}