package com.example.dsmabsen.network

import com.example.dsmabsen.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email:String,
        @Field("password") password:String,
        @Field("imei") imei:String
    ): Response<LoginResponse>
}