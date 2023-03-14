package com.example.dsmabsen.helper

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dsmabsen.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.handleApiError(
    error: String?
) {
    when (error) {
        getString(R.string.failed_connect) -> {
            requireView().snackbar(error)
        }
        getString(R.string.bad_connection) -> requireView().snackbar(error)
        getString(R.string.tidak_berada_diwaktu_presensi) -> requireView().snackbar(error)

        else -> requireView().snackbar(error!!)
    }
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun Activity.handleApiErrorActivity(
    error: String?
) {
    when (error) {
        getString(R.string.failed_connect) -> Toast.makeText(
            this,
            "can`t connect to server, maybe server is bad server",
            Toast.LENGTH_LONG
        ).show()
        getString(R.string.bad_connection) -> Toast.makeText(
            this,
            "Bad connection, check your connection",
            Toast.LENGTH_LONG
        )
            .show()
        else -> Toast.makeText(this, "$error", Toast.LENGTH_LONG).show()
    }
}