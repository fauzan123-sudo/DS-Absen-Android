package com.example.dsmabsen.network

import com.example.dsmabsen.model.HomeResponse
import com.example.dsmabsen.model.PasswordCheck
import com.example.dsmabsen.model.PresensiHariIni
import com.example.dsmabsen.model.UbahPassword
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PasswordApi {

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
    ): Response<PasswordCheck>


}