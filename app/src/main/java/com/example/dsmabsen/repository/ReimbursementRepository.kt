package com.example.dsmabsen.repository

import com.example.dsmabsen.network.ReimbursementApi
import com.example.dsmabsen.network.UserProfileApi
import javax.inject.Inject

class ReimbursementRepository @Inject constructor(private val api: ReimbursementApi) :
    BaseRepository() {

    suspend fun getListReimbursement(nip: String) =
        safeApiCall {
            api.getListReimbursement(nip)
        }

//    suspend fun getDetailProfile(nip: String) =
//        safeApiCall {
//            api.getDetailProfile(nip)
//        }
}