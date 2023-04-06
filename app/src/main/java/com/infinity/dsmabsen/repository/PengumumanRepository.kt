package com.infinity.dsmabsen.repository

import com.infinity.dsmabsen.network.PengumumanApi
import javax.inject.Inject

class PengumumanRepository @Inject constructor(private val api: PengumumanApi) :
    BaseRepository() {

    suspend fun getPengumuman() =
        safeApiCall {
            api.getPengumuman()
        }

}