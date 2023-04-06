package com.infinity.dsmabsen.repository

import com.infinity.dsmabsen.network.UserProfileApi
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

    suspend fun editDataProfile(nip: String, name: String, value: String) = safeApiCall {
        api.editProfile(nip, name, value)
    }
}