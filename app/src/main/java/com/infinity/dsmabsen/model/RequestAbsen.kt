package com.infinity.dsmabsen.model

import java.io.File

data class RequestAbsen(
    val image: File?,
    val nip: String?,
    val date: String?,
    val timezone: String?,
    val kordinat: String?,
    val kode_shift: String?,
    val kode_tingkat: String?
)
