package com.example.dsmabsen.repository

import com.example.dsmabsen.model.RequestAbsen
import com.example.dsmabsen.network.DataAttendanceApi
import com.example.dsmabsen.network.HomeApi
import com.example.dsmabsen.network.PasswordApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
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