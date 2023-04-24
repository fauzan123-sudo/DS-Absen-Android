package com.infinity.dsmabsen.network

import com.infinity.dsmabsen.model.AktivitasResponse
import com.infinity.dsmabsen.model.SendAktivitasResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AktivitasApi {
    @GET("aktifitas")
    suspend fun listAktivitas(
        @Query("nip") nip: String
    ): Response<AktivitasResponse>

    @Multipart
    @POST("aktifitas/store")
    suspend fun addAktifitas(
        @Part("nip") nip: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("koordinat") koordinat:RequestBody,
        @Part foto: MultipartBody.Part
    ) : Response<SendAktivitasResponse>
}