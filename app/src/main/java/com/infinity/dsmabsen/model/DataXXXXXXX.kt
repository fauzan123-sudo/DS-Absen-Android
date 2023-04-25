package com.infinity.dsmabsen.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataXXXXXXX(
    val `file`: String?,
    val id: Int?,
    val keterangan: String?,
    val komentar: String?,
    val nilai: String?,
    val reimbursement: String?,
    val status: String?,
    val created_at:String?
) : Parcelable