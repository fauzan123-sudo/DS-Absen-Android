package com.infinity.dsmabsen.model

data class Pendidikan(
    val `file`: String?,
    val id: Int?,
    val is_akhir: Int?,
    val jurusan: Jurusan?,
    val nama_sekolah: String?,
    val nip: String?,
    val nomor_ijazah: String?,
    val pendidikan: PendidikanX,
    val tanggal_lulus: String?
)