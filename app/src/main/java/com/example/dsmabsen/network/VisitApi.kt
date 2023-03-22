package com.example.dsmabsen.network

import com.example.dsmabsen.model.SpinnerVisit
import com.example.dsmabsen.model.VisitResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VisitApi {

    @GET("visit")
    suspend fun getVisit(
        @Query("nip") nip: String
    ): Response<VisitResponse>

    @GET("list-lokasi-visit")
    suspend fun spinnerVisit(
    ): Response<SpinnerVisit>


}