package com.infinity.dsmabsen.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.DateFormat
import java.util.*

object LocationUtils {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    fun getActualLocation(
        context: Context,
        successCallback: (Double, Double) -> Unit,
        errorCallback: () -> Unit
    ) {
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
                getTime(context, it.latitude, it.longitude)
                successCallback(it.latitude, it.longitude)
            } else {
                errorCallback()
            }
        }
    }

    private fun getTime(context: Context, latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val timeZone = TimeZone.getDefault()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.timeZone = timeZone

        // Tampilkan waktu saat ini dalam format yang diinginkan
        val time = DateFormat.getTimeInstance().format(calendar.time)
        val date = DateFormat.getDateInstance().format(calendar.time)
        val address = addresses?.get(0)?.getAddressLine(0)

        Log.d("LocationUtils", "Waktu: $time, Tanggal: $date, Alamat: $address")
    }
}

//  try {
//            LocationUtils.getActualLocation(this,
//                { latitude, longitude ->
//                    binding.lattitude.text = "latitude : $latitude"
//                    binding.longitude.text = "longitude : $longitude"
//                },
//                {
//                    Toast.makeText(this, "Gagal mendapatkan lokasi", Toast.LENGTH_SHORT).show()
//                })
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }

