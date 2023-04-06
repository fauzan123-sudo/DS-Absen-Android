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

    @FormUrlEncoded
    @POST("visit/store")
    suspend fun sendDataVisit(
        @Field("nip") nip: String,
        @Field("kode_visit") kode_visit: String,
//        @Field("time_zone") time_zone: RequestBody,
        @Field("kordinat") kordinat: String,
//        @Field image: MultipartBody.Part
    ) : Response<ResponsePengajuanIzin>

}