package com.infinity.dsmabsen.repository

import com.infinity.dsmabsen.network.PerizinanApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class DataPerizinanRepository @Inject constructor(private val api: PerizinanApi) :
    BaseRepository() {

    suspend fun dataPerizinan(nip: String) =
        safeApiCall { api.dataPerizinan(nip) }

    suspend fun getSpinner() =
        safeApiCall { api.getSpinner() }

    suspend fun sendPermission(
        nip: RequestBody,
        kode_cuti: RequestBody,
        tanggal_mulai: RequestBody,
        tanggal_selesai: RequestBody,
        file: MultipartBody.Part,
        keterangan: RequestBody
    ) =
        safeApiCall { api.sendPermission(nip,kode_cuti,tanggal_mulai,tanggal_selesai,file,keterangan) }

}