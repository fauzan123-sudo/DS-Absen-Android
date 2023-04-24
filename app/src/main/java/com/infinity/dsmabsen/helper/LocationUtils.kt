package com.infinity.dsmabsen.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

object LocationUtils {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    fun getActualLocation(context: Context, successCallback: (Double, Double) -> Unit, errorCallback: () -> Unit) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val task = if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            errorCallback()
            return
        } else {
            fusedLocationProviderClient.lastLocation
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {

            errorCallback()
            return
        }

        task.addOnSuccessListener {
            if (it != null) {
                successCallback(it.latitude, it.longitude)
            } else {
                errorCallback()
            }
        }
    }
}
