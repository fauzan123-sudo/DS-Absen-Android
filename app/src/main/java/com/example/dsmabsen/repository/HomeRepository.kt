package com.example.dsmabsen.repository

import com.example.dsmabsen.network.HomeApi
import javax.inject.Inject

class HomeRepository @Inject constructor(private val api: HomeApi) : BaseRepository() {

    suspend fun homeUser(nip:String) =
        safeApiCall { api.homeUser(nip) }
}