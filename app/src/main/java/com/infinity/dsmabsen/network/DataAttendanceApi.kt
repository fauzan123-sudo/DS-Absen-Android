package com.infinity.dsmabsen.network

import com.infinity.dsmabsen.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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

    @GET("payroll-client")
    suspend fun getSallary(
        @Query("nip") nip: String
    ): Response<DataSallary>

    @GET("pengajuan/lembur/lists")
    suspend fun getListLembur(
        @Query("nip") nip: String
    ): Response<DataLembur>

    @Multipart
    @POST("pengajuan/lembur/store")
    suspend fun pengajuanLembur(
        @Part("nip") nip: RequestBody,
        @Part("jam_mulai") jam_mulai: RequestBody,
        @Part("jam_selesai") jam_selesai: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("tanggal") tanggal: RequestBody,
        @Part("keterangan") keterangan: RequestBody

    ): Response<PengajuanLembur>
}