package com.infinity.dsmabsen.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataXXXXXXXXXXXX(
    val cuti: String?,
    val `file`: String?,
    val id: Int?,
    val keterangan: String?,
    val komentar: String?,
    val status: String?,
    val tanggal_mulai: String?,
    val tanggal_selesai: String?,
    val created_at:String?
) : Parcelable