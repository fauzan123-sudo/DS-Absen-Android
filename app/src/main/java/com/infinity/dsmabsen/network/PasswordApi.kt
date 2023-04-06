package com.infinity.dsmabsen.network

import com.infinity.dsmabsen.model.PasswordCheck
import com.infinity.dsmabsen.model.UbahPassword
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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