package com.infinity.dsmabsen.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX(
    val foto: String?,
    val id: Int?,
    val jam: String?,
    val kode_visit: String?,
    val kordinat: String?,
    val nip: String?,
    val tanggal: String?,
    val visit: String?
) : Parcelable