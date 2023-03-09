package com.example.dsmabsen.network

import com.example.dsmabsen.model.ListReimbursement
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

interface ReimbursementApi {

    @GET("pengajuan/reimbursement/lists")
    suspend fun getListReimbursement(
        @Path("nip") nip: String
    ) : Response<ListReimbursement>
}