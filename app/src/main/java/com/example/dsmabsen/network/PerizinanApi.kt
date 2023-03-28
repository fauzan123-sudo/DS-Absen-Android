package com.example.dsmabsen.network

import com.example.dsmabsen.model.DataXXXXXXXXXX
import com.example.dsmabsen.model.Perizinan
import com.example.dsmabsen.model.ResponsePengajuanIzin
import com.example.dsmabsen.model.SpinnerIzin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface PerizinanApi {

    @GET("pengajuan/cuti/lists")
    suspend fun dataPerizinan(
        @Query("nip") nip: String
    ): Response<Perizinan>

    @GET("pengajuan/cuti")
    suspend fun getSpinner(
    ): Response<SpinnerIzin>

    @Multipart
    @POST("pengajuan/cuti/store")
    suspend fun sendPermission(
        @Part("nip") nip: RequestBody,
        @Part("kode_cuti") kode_cuti: RequestBody,
        @Part("tanggal_mulai") tanggal_mulai: RequestBody,
        @Part("tanggal_selesai") tanggal_selesai: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("keterangan") keterangan: RequestBody
    ): Response<ResponsePengajuanIzin>
}