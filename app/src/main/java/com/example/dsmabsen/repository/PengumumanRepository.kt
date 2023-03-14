package com.example.dsmabsen.repository

import com.example.dsmabsen.network.PengumumanApi
import com.example.dsmabsen.network.UserProfileApi
import javax.inject.Inject

class PengumumanRepository @Inject constructor(private val api: PengumumanApi) :
    BaseRepository() {

    suspend fun getPengumuman() =
        safeApiCall {
            api.getPengumuman()
        }

}