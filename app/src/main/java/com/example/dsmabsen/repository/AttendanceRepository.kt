package com.example.dsmabsen.repository

import com.example.dsmabsen.network.DataAttendanceApi
import com.example.dsmabsen.network.HomeApi
import javax.inject.Inject

class AttendanceRepository @Inject constructor(private val api: DataAttendanceApi) : BaseRepository() {

    suspend fun attendanceTotal(nip: String) =
        safeApiCall { api.attendanceTotal(nip) }

    suspend fun attendanceHistory(nip: String) =
        safeApiCall { api.attendanceHistory(nip) }

}