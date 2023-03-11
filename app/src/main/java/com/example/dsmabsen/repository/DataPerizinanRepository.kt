package com.example.dsmabsen.repository

import com.example.dsmabsen.model.RequestAbsen
import com.example.dsmabsen.network.DataAttendanceApi
import com.example.dsmabsen.network.HomeApi
import com.example.dsmabsen.network.PerizinanApi
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
        nip: String,
        kode_cuti: String,
        tanggal_mulai:String,
        tanggal_selesai:String,
        file: String,
        keterangan: String
    ) =
        safeApiCall { api.sendPermission(nip,kode_cuti,tanggal_mulai,tanggal_selesai,file,keterangan) }

}