package com.infinity.dsmabsen.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataXXXXXXXXXXXXXXXXXXXXXX(
    val `file`: String?,
    val id: Int?,
    val is_akhir: Int?,
    val keterangan: String?,
    val kode_status: Int?,
    val komentar: String?,
    val nama: String?,
    val nip: String?,
    val nomor_surat: String?,
    val shift: String?,
    val status: String?,
    val status_api: String?,
    val tanggal_surat: String?,
    val created_at:String?
) : Parcelable