package com.example.dsmabsen.network

import com.example.dsmabsen.model.HomeResponse
import com.example.dsmabsen.model.PresensiHariIni
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeApi {
    @FormUrlEncoded
    @POST("home-user")
    suspend fun homeUser(
        @Field("nip") nip: String,
    ): Response<HomeResponse>

    @GET("absen/{nip}")
    suspend fun getAbsen(
        @Path("nip") nip:String
    ) : Response<PresensiHariIni>

//    @GET("absen/{nip}")
//    suspend fun getAbsen(
//        @Path("nip") nip:String
//    ) : Response<PresensiHariIni>


}