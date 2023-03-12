package com.example.dsmabsen.network

import com.example.dsmabsen.model.*
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
import retrofit2.http.Query

interface DataAttendanceApi {

    @GET("riwayat-presensi/{nip}")
    suspend fun attendanceHistory(
        @Path("nip") nip: String
    ): Response<ResponseRiwayat>

    @GET("riwayat-presensi/{nip}")
    suspend fun getAttendanceHistory(
        @Path("nip") nip: String
    ): Response<ResponseRiwayat>

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

    @GET("payroll-client/index")
    suspend fun getSallary(
        @Query("nip") nip: String
    ): Response<DataSallary>

    @GET("pengajuan/lembur/lists")
    suspend fun getListLembur(
        @Query("nip") nip: String
    ): Response<DataLembur>

    @FormUrlEncoded
    @POST("pengajuan/lembur/store")
    suspend fun pengajuanLembur(
        @Field("nip") nip: String,
        @Field("jam_mulai") jam_mulai: String,
        @Field("jam_selesai") jam_selesai: String,
        @Field("file") file: String,
        @Field("tanggal") tanggal: String,
        @Field("keterangan") keterangan: String

    ): Response<PengajuanLembur>

}