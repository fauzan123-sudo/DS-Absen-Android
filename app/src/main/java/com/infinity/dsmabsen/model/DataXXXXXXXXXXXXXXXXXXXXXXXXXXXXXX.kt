package com.infinity.dsmabsen.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX(
    val created_at: String,
    val deskripsi: String,
    val `file`: String,
    val id: Int,
    val judul: String
) : Parcelable