package com.infinity.dsmabsen.repository

import com.infinity.dsmabsen.network.AuthApi
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: AuthApi) : BaseRepository() {

    suspend fun loginUser(email: String, password: String, imei: String) =
        safeApiCall { api.loginUser(email, password, imei) }

    suspend fun logOut(nip: String) =
        safeApiCall { api.logOut(nip) }


    suspend fun logOuts(nip: String) =
        api.logOuts(nip)

    suspend fun ubahPassword(
        nip: String,
        password_lama: String,
        password_baru: String
    ) =
        safeApiCall { api.ubahPassword(nip, password_lama, password_baru) }

    suspend fun passwordCheck(nip: String) =
        safeApiCall { api.passwordCheck(nip) }
}