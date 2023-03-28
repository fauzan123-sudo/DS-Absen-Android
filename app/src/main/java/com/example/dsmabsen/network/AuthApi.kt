package com.example.dsmabsen.network

import com.example.dsmabsen.model.*
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
    ) : Response<ResponseBody>

    @FormUrlEncoded
    @POST("logout")
    suspend fun logOuts(
        @Field("nip") nip:String
    ) : ResponseBody

    @FormUrlEncoded
    @POST("ubah-password")
    suspend fun ubahPassword(
        @Field("nip") nip:String,
        @Field("password_lama") password_lama:String,
        @Field("password_baru") password_baru:String
    ) : Response<UbahPassword>

    @FormUrlEncoded
    @POST("password-check")
    suspend fun passwordCheck(
        @Field("nip") nip:String
    ): Response<ResponseBody>
//    ): Response<PasswordCheck>

}