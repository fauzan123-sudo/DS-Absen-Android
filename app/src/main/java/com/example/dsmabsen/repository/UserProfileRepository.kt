package com.example.dsmabsen.repository

import com.example.dsmabsen.network.UserProfileApi
import javax.inject.Inject

class UserProfileRepository @Inject constructor(private val api: UserProfileApi) :
    BaseRepository() {

    suspend fun getUserProfile(nip: String) =
        safeApiCall {
            api.getUserProfile(nip)
        }

    suspend fun getDetailProfile(nip: String) =
        safeApiCall {
            api.getDetailProfile(nip)
        }
}