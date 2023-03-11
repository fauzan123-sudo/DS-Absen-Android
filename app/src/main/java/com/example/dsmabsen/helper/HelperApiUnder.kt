package com.example.dsmabsen.helper

import android.os.Build
import androidx.annotation.RequiresApi
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.TextStyle
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class HelperApiUnder {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayApi23(): String {
        val indonesia = Locale("in", "ID")
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val today = LocalDate.now(ZoneId.systemDefault())
        val hariIni = today.dayOfWeek.getDisplayName(TextStyle.FULL, indonesia)
        val tanggal = today.format(formatter)
        val hasil = "$hariIni, $tanggal"
        return hariIni
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeDetail(): String {
        val indonesia = Locale("in", "ID")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val today = LocalDate.now(ZoneId.systemDefault())
        val hariIni = today.dayOfWeek.getDisplayName(TextStyle.FULL, indonesia)
        val tanggal = today.format(formatter)
        val hasil = "$hariIni, $tanggal"
        return hariIni
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(): String? {
        val formatter = DateTimeFormatter.ofPattern("dd")
        val today = LocalDate.now(ZoneId.systemDefault())
        return today.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMY(): String? {
        val formatter = DateTimeFormatter.ofPattern("MM - yyyy")
        val today = LocalDate.now(ZoneId.systemDefault())
        return today.format(formatter)
    }
}