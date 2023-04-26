package com.infinity.dsmabsen.network

import com.infinity.dsmabsen.model.ResponsePengajuanIzin
import com.infinity.dsmabsen.model.SpinnerVisit
import com.infinity.dsmabsen.model.VisitResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface VisitApi {

    @GET("visit")
    suspend fun getVisit(
        @Query("nip") nip: String
    ): Response<VisitResponse>

    @GET("list-lokasi-visit")
    suspend fun spinnerVisit(
    ): Response<SpinnerVisit>

    @Multipart
    @POST("visit/store")
    suspend fun sendDataVisit(
        @Part("nip") nip: RequestBody,
        @Part("kode_visit") kode_visit: RequestBody,
        @Part("kordinat") kordinat: RequestBody,
        @Part image: MultipartBody.Part
    ) : Response<ResponsePengajuanIzin>

}