package com.example.dsmabsen.network

import com.example.dsmabsen.model.Pengumuman
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PengumumanApi {

    @GET("pengumuman")
    suspend fun getPengumuman(
    ) : Response<Pengumuman>
}