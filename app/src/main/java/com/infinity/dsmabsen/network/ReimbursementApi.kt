package com.infinity.dsmabsen.network

import com.infinity.dsmabsen.model.ListReimbursement
import com.infinity.dsmabsen.model.PengajuanReimbusement
import com.infinity.dsmabsen.model.SpinnerReimbursement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ReimbursementApi {

    @GET("pengajuan/reimbursement/lists")
    suspend fun getListReimbursement(
        @Query("nip") nip: String
    ) : Response<ListReimbursement>

    @Multipart
    @POST("pengajuan/reimbursement/store")
    suspend fun pengajuanReimbursement(
        @Part("nip") nip: RequestBody,
        @Part("kode_reimbursement") kode_reimbursement: RequestBody,
        @Part("nilai") nilai: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("keterangan") keterangan: RequestBody
    ) : Response<PengajuanReimbusement>

    @GET("pengajuan/reimbursement")
    suspend fun spinnerReimbursement(

    ):Response<SpinnerReimbursement>



}