package com.infinity.dsmabsen.repository

import com.infinity.dsmabsen.network.DataAttendanceApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import javax.inject.Inject

class AttendanceRepository @Inject constructor(private val api: DataAttendanceApi) :
    BaseRepository() {

    suspend fun attendanceTotal(nip: String) =
        safeApiCall { api.attendanceTotal(nip) }

    suspend fun getSallary(nip: String) =
        safeApiCall { api.getSallary(nip) }

    suspend fun getAttendanceHistory(nip: String) =
        safeApiCall { api.getAttendanceHistory(nip) }


    suspend fun attendanceHistory(nip: String) =
        safeApiCall { api.attendanceHistory(nip) }

    suspend fun attendanceToday2(
        image: MultipartBody.Part,
        nip: RequestBody,
        date: RequestBody,
        timezone: RequestBody,
        kordinat: RequestBody,
        kode_shift: RequestBody,
        kode_tingkat: RequestBody
    ) = safeApiCall {
        api.attendanceToday(
            image,
            nip,
            date,
            timezone,
            kordinat,
            kode_shift,
            kode_tingkat
        )
    }


    suspend fun getListLembur(nip: String) =
        safeApiCall { api.getListLembur(nip) }

    suspend fun pengajuanLembur(
        nip: RequestBody,
        jam_mulai: RequestBody,
        jam_selesai: RequestBody,
        file: MultipartBody.Part,
        tanggal: RequestBody,
        keterangan: RequestBody
    ) =
        safeApiCall { api.pengajuanLembur(nip, jam_mulai, jam_selesai, file, tanggal, keterangan) }

    suspend fun attendanceToday(
        image: MultipartBody.Part,
        nip: RequestBody,
        date: RequestBody,
        timezone: RequestBody,
        kordinat: RequestBody,
        kode_shift: RequestBody,
        kode_tingkat: RequestBody
    ) =
        safeApiCall {
            api.attendanceToday(
                image,
                nip,
                date,
                timezone,
                kordinat,
                kode_shift,
                kode_tingkat
            )
        }

}