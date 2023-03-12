package com.example.dsmabsen.ui.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dsmabsen.ui.fragment.AttendanceFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.findNavController()

//        Bottom Navigation
        with(binding) {
            bottomNavigationView.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
            }

            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (navController.currentDestination?.id == R.id.berandaFragment) {
                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setTitle("Konfirmasi")
                        builder.setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                        builder.setPositiveButton("Ya") { _, _ ->
                            finish()
                        }

                        builder.setNegativeButton("Tidak") { dialog, _ ->
                            dialog.dismiss()
                        }

                        val dialog = builder.create()
                        dialog.show()
                    } else {
                        navController.navigateUp()
                    }
                }
            }
            onBackPressedDispatcher.addCallback(this@MainActivity, callback)
        }

            navController.addOnDestinationChangedListener { _, destination, _ ->

                val arrayHideNavbar = arrayOf(
                    R.id.berandaFragment,
                    R.id.attendanceFragment,
                    R.id.dataAbsenFragment,
                )
                val arrayFragmentBgWhite = arrayOf(
                    R.id.profileFragment,
                    R.id.allProfileFragment,
                    R.id.personalDataFragment,
                )

                if (arrayHideNavbar.contains(destination.id)) {

                    binding.bottomNavigationView.visibility = View.VISIBLE
                } else {
                    binding.bottomNavigationView.visibility = View.GONE

                }
                if (arrayFragmentBgWhite.contains(destination.id)) {
                    binding.main.setBackgroundColor(Color.WHITE)

                } else {

                    binding.main.background = AppCompatResources.getDrawable(applicationContext,R.drawable.gradiend_background)
                }
            }

//            bottomNavigationView.setOnItemSelectedListener { item ->
//                when (item.itemId) {
//                    R.id.berandaFragment -> navController.navigate(R.id.berandaFragment)
//                    R.id.dataAbsenFragment -> navController.navigate(R.id.action_berandaFragment_to_dataAbsenFragment)
//                }
//                true
//            }


//            bottomNavigationView.setOnItemSelectedListener { item ->
//                when (item.itemId) {
//                    R.id.berandaFragment -> {
//                        val navOptions = NavOptions.Builder()
//                            .setEnterAnim(R.anim.slide_in_left)
//                            .setExitAnim(R.anim.slide_out_right)
//                            .setPopEnterAnim(R.anim.slide_in_right)
//                            .setPopExitAnim(R.anim.slide_out_left)
//                            .build()
//                        navController.navigate(R.id.berandaFragment, null, navOptions)
//                    }
//                    R.id.dataAbsenFragment -> {
//                        val navOptions = NavOptions.Builder()
//                            .setEnterAnim(R.anim.slide_in_right)
//                            .setExitAnim(R.anim.slide_out_left)
//                            .setPopEnterAnim(R.anim.slide_in_left)
//                            .setPopExitAnim(R.anim.slide_out_right)
//                            .build()
//
//                        navController.navigate(R.id.dataAbsenFragment, null, navOptions)
//                    }
//                }
//                true
//
//            }

    }
//        //Tambahkan kode berikut untuk menangani event tombol kembali di fragment Beranda
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                val builder = AlertDialog.Builder(this@MainActivity)
//                builder.setTitle("Konfirmasi Keluar")
//                    .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
//                    .setPositiveButton("Ya") { _, _ ->
//                        finish()
//                    }
//                    .setNegativeButton("Tidak") { dialog, _ ->
//                        dialog.dismiss()
//                    }
//                builder.create().show()
//            }
//        }
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.id == R.id.berandaFragment) {
//                onBackPressedDispatcher.addCallback(this@MainActivity, callback)
//            } else {
//                onBackPressedDispatcher.onBackPressed()
//            }
//        }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}