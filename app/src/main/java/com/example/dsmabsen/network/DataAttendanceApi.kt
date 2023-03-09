package com.example.dsmabsen.network

import com.example.dsmabsen.model.AttendanceHistory
import com.example.dsmabsen.model.TotalAttendance
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DataAttendanceApi {


    @GET("riwayat-presensi/{nip}")
    suspend fun attendanceHistory(
        @Path("nip") nip: String
    ) : Response<AttendanceHistory>

    @GET("total-presensi/{nip}")
    suspend fun attendanceTotal(
        @Path("nip") nip: String
    ): Response<TotalAttendance>
}