package com.example.dsmabsen.network

import com.example.dsmabsen.model.HomeResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HomeApi {
    @FormUrlEncoded
    @POST("home-user")
    suspend fun homeUser(
        @Field("nip") nip: String,
    ): Response<HomeResponse>
}