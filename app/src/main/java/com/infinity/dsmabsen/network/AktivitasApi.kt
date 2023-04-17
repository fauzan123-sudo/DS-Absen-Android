package com.infinity.dsmabsen.network

import com.infinity.dsmabsen.model.AktivitasResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AktivitasApi {
    @GET("aktifitas")
    suspend fun listAktivitas(
        @Query("nip") nip: String
    ): Response<AktivitasResponse>
}