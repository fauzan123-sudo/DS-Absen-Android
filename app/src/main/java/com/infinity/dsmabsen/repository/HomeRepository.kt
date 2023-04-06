package com.infinity.dsmabsen.repository

import com.infinity.dsmabsen.network.HomeApi
import javax.inject.Inject

class HomeRepository @Inject constructor(private val api: HomeApi) : BaseRepository() {

    suspend fun homeUser(nip:String) =
        safeApiCall { api.homeUser(nip) }

    suspend fun getAbsen(nip:String) =
        safeApiCall { api.getAbsen(nip) }
}