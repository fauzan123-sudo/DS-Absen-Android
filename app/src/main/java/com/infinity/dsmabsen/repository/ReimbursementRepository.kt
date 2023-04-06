package com.infinity.dsmabsen.repository

import com.infinity.dsmabsen.network.ReimbursementApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ReimbursementRepository @Inject constructor(private val api: ReimbursementApi) :
    BaseRepository() {

    suspend fun getListReimbursement(nip: String) =
        safeApiCall {
            api.getListReimbursement(nip)
        }

    suspend fun pengajuanReimbursement(nip: RequestBody,
                                       kode_reimbursement: RequestBody,
                                       nilai: RequestBody,
                                       file: MultipartBody.Part,
                                       keterangan: RequestBody) =
        safeApiCall {
            api.pengajuanReimbursement(nip, kode_reimbursement, nilai, file, keterangan)
        }

    suspend fun spinnerReimbursement() =
        safeApiCall {
            api.spinnerReimbursement()
        }
}