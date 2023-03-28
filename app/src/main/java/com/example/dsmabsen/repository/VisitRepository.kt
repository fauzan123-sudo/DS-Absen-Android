package com.example.dsmabsen.repository

import com.example.dsmabsen.model.RequestAbsen
import com.example.dsmabsen.network.DataAttendanceApi
import com.example.dsmabsen.network.HomeApi
import com.example.dsmabsen.network.VisitApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
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
        nip: RequestBody,
        kode_cuti: RequestBody,
        timezone:RequestBody,
        kordinat:RequestBody,
        image: MultipartBody.Part
    ) =
        safeApiCall {
            api.sendDataVisit(nip, kode_cuti, timezone, kordinat, image)
        }

}