package com.infinity.dsmabsen.network

import com.infinity.dsmabsen.model.PengajuanShift
import com.infinity.dsmabsen.model.Shift
import com.infinity.dsmabsen.model.SpinnerShift
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShiftApi {

    @GET("pengajuan/shift/lists")
    suspend fun getShift(
        @Query("nip") nip: String
    ): Response<Shift>

    @FormUrlEncoded
    @POST("pengajuan/shift/store")
    suspend fun pengajuanShift(
        @Field("nip") nip: String,
        @Field("kode_cuti") kode_cuti: String,
        @Field("file") file: String,
        @Field("keterangan") keterangan: String
    ) : Response<PengajuanShift>

    @GET("pengajuan/shift")
    suspend fun spinnerShift(

    ) : Response<SpinnerShift>
}