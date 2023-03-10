package com.example.dsmabsen.repository

import com.example.dsmabsen.model.RequestAbsen
import com.example.dsmabsen.network.DataAttendanceApi
import com.example.dsmabsen.network.HomeApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AttendanceRepository @Inject constructor(private val api: DataAttendanceApi) :
    BaseRepository() {

    suspend fun attendanceTotal(nip: String) =
        safeApiCall { api.attendanceTotal(nip) }

    suspend fun attendanceHistory(nip: String) =
        safeApiCall { api.attendanceHistory(nip) }

    suspend fun attendanceToday(
//        data:RequestAbsen
        image: MultipartBody.Part,
        nip: RequestBody,
        date: RequestBody,
        timezone: RequestBody,
        kordinat: RequestBody,
        kode_shift: RequestBody,
        kode_tingkat: RequestBody
    ) =
        safeApiCall { api.attendanceToday(image,nip,date, timezone, kordinat, kode_shift, kode_tingkat) }

}