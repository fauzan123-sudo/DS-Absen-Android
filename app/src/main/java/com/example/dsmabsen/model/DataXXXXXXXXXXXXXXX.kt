package com.example.dsmabsen.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DataXXXXXXXXXXXXXXX(
    val Dis_aktif: String,
    val divisi: String,
    val gaji_pokok: String,
    val id: String,
    val is_aktif: String,
    val jabatan: String,
    val kode_payroll: String,
    val nama: String,
    val nip: String,
    val slip_url: String,
    val tanggal: String,
    val total: String,
    val total_penambahan: String,
    val total_potongan: String
) : Parcelable