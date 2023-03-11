package com.example.dsmabsen.network

import com.example.dsmabsen.model.DataXXXXXXXXXX
import com.example.dsmabsen.model.Perizinan
import com.example.dsmabsen.model.ResponsePengajuanIzin
import com.example.dsmabsen.model.SpinnerIzin
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PerizinanApi {

    @GET("pengajuan/cuti/lists")
    suspend fun dataPerizinan(
        @Query("nip") nip: String
    ): Response<Perizinan>

    @GET("pengajuan/cuti")
    suspend fun getSpinner(
    ): Response<SpinnerIzin>

    @FormUrlEncoded
    @POST("pengajuan/cuti/store")
    suspend fun sendPermission(
        @Field("nip") nip: String,
        @Field("kode_cuti") kode_cuti: String,
        @Field("tanggal_mulai") tanggal_mulai: String,
        @Field("tanggal_selesai") tanggal_selesai: String,
        @Field("file") file: String,
        @Field("keterangan") keterangan: String
    ): Response<ResponsePengajuanIzin>
}