package com.example.dsmabsen.repository

import com.example.dsmabsen.network.ReimbursementApi
import com.example.dsmabsen.network.UserProfileApi
import retrofit2.http.Field
import javax.inject.Inject

class ReimbursementRepository @Inject constructor(private val api: ReimbursementApi) :
    BaseRepository() {

    suspend fun getListReimbursement(nip: String) =
        safeApiCall {
            api.getListReimbursement(nip)
        }

    suspend fun pengajuanReimbursement(nip: String,
                                       kode_reimbursement: String,
                                       nilai: String,
                                       file: String,
                                       keterangan: String) =
        safeApiCall {
            api.pengajuanReimbursement(nip, kode_reimbursement, nilai, file, keterangan)
        }
}