package com.example.dsmabsen

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import io.paperdb.Paper

@HiltAndroidApp
class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
        AndroidThreeTen.init(this)
    }
}