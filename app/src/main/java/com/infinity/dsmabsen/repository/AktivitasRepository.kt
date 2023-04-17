package com.infinity.dsmabsen.repository

import com.infinity.dsmabsen.network.AktivitasApi
import com.infinity.dsmabsen.network.PerizinanApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AktivitasRepository @Inject constructor(private val api: AktivitasApi) :
    BaseRepository() {

    suspend fun listAktivitas(nip: String) =
        safeApiCall { api.listAktivitas(nip) }


}