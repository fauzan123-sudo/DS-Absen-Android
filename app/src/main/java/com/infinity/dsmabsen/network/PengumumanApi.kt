package com.infinity.dsmabsen.network

import com.infinity.dsmabsen.model.Pengumuman
import retrofit2.Response
import retrofit2.http.GET

interface PengumumanApi {

    @GET("pengumuman")
    suspend fun getPengumuman(
    ) : Response<Pengumuman>
}