package com.example.dsmabsen.network

import com.example.dsmabsen.model.LoginResponse
import com.example.dsmabsen.model.Logout
import okhttp3.ResponseBody
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

    @FormUrlEncoded
    @POST("logout")
    suspend fun logOut(
        @Field("nip") nip:String
    ) : Response<Logout>


    @POST("logout")
    suspend fun logOuts(
        @Field("nip") nip:String
    ) : ResponseBody
}