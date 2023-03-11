package com.example.dsmabsen.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.dsmabsen.ui.activity.MainActivity
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class Helper {

    fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
        Intent(this, activity).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    companion object {
        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun hideKeyboard(view: View) {
            try {
                val imm =
                    view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (e: Exception) {

            }
        }

        const val keyName = "checkOut"
    }

    fun gantiRupiah(string: String): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(Integer.valueOf(string))
//            .replace("Rp","Rp. ")
//            .replace(",00","")
    }

    fun changeToRupiah(change: String): String {
        return NumberFormat.getNumberInstance(Locale.US)
            .format(change)
            .replace(",", ".")
    }

    fun gantiRupiah(value: Int): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value)
    }

    fun gantiRupiah(value: Boolean): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value)
    }

    fun setToolbar(activity: Activity, toolbar: Toolbar, title: String) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        activity.supportActionBar!!.title = title
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDay(): String? {
        val hariIni = LocalDate.now().dayOfWeek
        val indonesia = Locale("in", "ID")
        return DayOfWeek.of(hariIni.value).getDisplayName(TextStyle.FULL, indonesia)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(): String? {
        val currentDate = LocalDate.now()
        return currentDate.format(DateTimeFormatter.ofPattern("dd"))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMY(): String? {
        val currentDate = LocalDate.now()
        return currentDate.format(DateTimeFormatter.ofPattern("MM - yyyy"))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMYDetail(): String {
        val currentDate = LocalDate.now()
        return currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    fun convertTanggal(
        tgl: String,
        newFormat: String,
        oldFormat: String = "yyyy-MM-dd hh:mm:ss"
    ): String {
        val dateFormat = SimpleDateFormat(oldFormat)
        val convert = dateFormat.parse(tgl)
        dateFormat.applyPattern(newFormat)
        return convert.let { dateFormat.format(it!!) }
    }

    fun convertDate(
        tgl: String,
        newFormat: String,
        oldFormat: String = "dd-MM-yyyy"
    ): String {
        val dateFormat = SimpleDateFormat(oldFormat)
        val convert = dateFormat.parse(tgl)
        dateFormat.applyPattern(newFormat)
        return convert.let { dateFormat.format(it) }
    }

    fun loadGlideImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .into(imageView)
    }

    fun permission() {

    }


}