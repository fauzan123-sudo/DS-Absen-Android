package com.infinity.dsmabsen.repository

import com.infinity.dsmabsen.network.VisitApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class VisitRepository @Inject constructor(private val api: VisitApi) :
    BaseRepository() {

    suspend fun getVisit(nip: String) =
        safeApiCall { api.getVisit(nip) }

    suspend fun spinnerVisit() =
        safeApiCall {
            api.spinnerVisit()
        }

    suspend fun sendDataVisit(
        nip: String,
        kode_visit: String,
//        timezone:RequestBody,
        kordinat:String,
//        image: MultipartBody.Part
    ) =
        safeApiCall {
            api.sendDataVisit(nip, kode_visit, kordinat)
        }

}