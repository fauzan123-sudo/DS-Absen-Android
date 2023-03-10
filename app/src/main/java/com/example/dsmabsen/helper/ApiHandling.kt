package com.example.dsmabsen.helper

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dsmabsen.R

fun Fragment.handleApiError(
    error: String?
) {
    when (error) {
        getString(R.string.failed_connect) -> Toast.makeText(
            context,
            "can`t connect to server, maybe server is bad server",
            Toast.LENGTH_LONG
        ).show()
        getString(R.string.bad_connection) -> Toast.makeText(
            context,
            "Bad connection, check your connection",
            Toast.LENGTH_LONG
        )
            .show()
        else -> Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
    }
}

fun Activity.handleApiError(
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