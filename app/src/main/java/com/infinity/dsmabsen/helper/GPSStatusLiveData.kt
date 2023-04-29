package com.infinity.dsmabsen.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData

class GPSStatusLiveData private constructor() : LiveData<Boolean>() {

    private lateinit var context: Context
    private val locationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val gpsStatusListener = object : android.location.GpsStatus.Listener {
        override fun onGpsStatusChanged(event: Int) {
            when (event) {
                android.location.GpsStatus.GPS_EVENT_STOPPED -> {
                    postValue(false)
                    showDialog()
                }
                else -> postValue(true)
            }
        }
    }

    companion object {
        private val instances: GPSStatusLiveData by lazy { GPSStatusLiveData() }
        private var hasInit = false

        fun init(context: Context) {
            if (!hasInit) {
                instances.context = context.applicationContext
                hasInit = true
            }
        }

        fun getInstance(): GPSStatusLiveData {
            return instances
        }
    }

    override fun onActive() {
        super.onActive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, request the permission
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
                return
            }
        }

        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGPSEnabled) {
            postValue(false)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locationManager.registerGnssStatusCallback(object :
                android.location.GnssStatus.Callback() {
                override fun onStarted() {
                    super.onStarted()
                    postValue(true)
                }

                override fun onStopped() {
                    super.onStopped()
                    postValue(false)
                    showDialog()
                }
            })
        } else {
            locationManager.addGpsStatusListener(gpsStatusListener)
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locationManager.unregisterGnssStatusCallback(object :
                android.location.GnssStatus.Callback() {})
        } else {
            locationManager.removeGpsStatusListener(gpsStatusListener)
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("GPS is disabled, do you want to enable it?")
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }
        builder.create().show()
    }
}





