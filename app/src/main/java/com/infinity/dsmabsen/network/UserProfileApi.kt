package com.infinity.dsmabsen.network

import com.infinity.dsmabsen.model.EditProfileResponse
import com.infinity.dsmabsen.model.ProfileDetail
import com.infinity.dsmabsen.model.ProfileUser
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserProfileApi {

    @GET("profil/{nip}")
    suspend fun getUserProfile(
        @Path("nip") nip: String
    ) : Response<ProfileUser>

    @GET("profil-detail/{nip}")
    suspend fun getDetailProfile(
        @Path("nip") nip: String
    ) :Response<ProfileDetail>

    @FormUrlEncoded
    @POST("edit-profil")
    suspend fun editProfile(
        @Field("nip") nip: String,
        @Field("name") name: String,
        @Field("value") value: String,
    ) : Response<EditProfileResponse>
}