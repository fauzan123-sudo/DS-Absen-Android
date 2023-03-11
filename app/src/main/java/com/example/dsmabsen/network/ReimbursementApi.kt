package com.example.dsmabsen.network

import com.example.dsmabsen.model.ListReimbursement
import com.example.dsmabsen.model.PengajuanReimbusement
import retrofit2.Response
import retrofit2.http.*

interface ReimbursementApi {

    @GET("pengajuan/reimbursement/lists")
    suspend fun getListReimbursement(
        @Query("nip") nip: String
    ) : Response<ListReimbursement>

    @FormUrlEncoded
    @POST("pengajuan/reimbursement/store")
    suspend fun pengajuanReimbursement(
        @Field("nip") nip: String,
        @Field("kode_reimbursement") kode_reimbursement: String,
        @Field("nilai") nilai: String,
        @Field("file") file: String,
        @Field("keterangan") keterangan: String
    ) : Response<PengajuanReimbusement>
}