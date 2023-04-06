package com.infinity.dsmabsen.repository

import com.infinity.dsmabsen.network.PasswordApi
import javax.inject.Inject

class PasswordRepository @Inject constructor(private val api: PasswordApi) :
    BaseRepository() {
    suspend fun ubahPassword(
        nip: String,
        password_lama: String,
        password_baru: String
    ) =
        safeApiCall { api.ubahPassword(nip, password_lama, password_baru) }

    suspend fun passwordCheck(nip: String) =
        safeApiCall { api.passwordCheck(nip) }

}