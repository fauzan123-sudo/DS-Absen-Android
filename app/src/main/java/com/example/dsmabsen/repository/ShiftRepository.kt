package com.example.dsmabsen.repository

import com.example.dsmabsen.network.ShiftApi
import retrofit2.http.Field
import javax.inject.Inject

class ShiftRepository @Inject constructor(private val api: ShiftApi) :
    BaseRepository() {

    suspend fun getListShift(nip: String) =
        safeApiCall {
            api.getShift(nip)
        }

    suspend fun pengajuanShift(
        nip: String,
        kode_cuti: String,
        file: String,
        keterangan: String
    ) =
        safeApiCall {
            api.pengajuanShift(nip, kode_cuti, file, keterangan)
        }

    suspend fun spinnerShift() =
        safeApiCall {
            api.spinnerShift()
        }

//    suspend fun getDetailProfile(nip: String) =
//        safeApiCall {
//            api.getDetailProfile(nip)
//        }
}