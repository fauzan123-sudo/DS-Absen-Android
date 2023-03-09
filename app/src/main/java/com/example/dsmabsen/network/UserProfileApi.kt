package com.example.dsmabsen.network

import com.example.dsmabsen.model.ProfileDetail
import com.example.dsmabsen.model.ProfileUser
import retrofit2.Response
import retrofit2.http.GET
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
}