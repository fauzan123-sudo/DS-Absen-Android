package com.example.dsmabsen.network

import com.example.dsmabsen.model.AttendanceHistory
import com.example.dsmabsen.model.Presensi
import com.example.dsmabsen.model.TotalAttendance
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface DataAttendanceApi {


    @GET("riwayat-presensi/{nip}")
    suspend fun attendanceHistory(
        @Path("nip") nip: String
    ): Response<AttendanceHistory>

    @GET("total-presensi/{nip}")
    suspend fun attendanceTotal(
        @Path("nip") nip: String
    ): Response<TotalAttendance>

    @Multipart
    @POST("presensi/store")
    suspend fun attendanceToday(
        @Part image: MultipartBody.Part,
        @Part("nip") nip: RequestBody,
        @Part("date") date: RequestBody,
        @Part("timezone") timezone: RequestBody,
        @Part("kordinat") kordinat: RequestBody,
        @Part("kode_shift") kode_shift: RequestBody,
        @Part("kode_tingkat") kode_tingkat: RequestBody,
    ): Response<Presensi>
}