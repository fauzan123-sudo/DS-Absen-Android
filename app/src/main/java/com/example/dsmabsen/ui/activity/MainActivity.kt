package com.example.dsmabsen.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dsmabsen.ui.fragment.AttendanceFragment

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
            bottomNavigationView.menu.getItem(1).isEnabled = false

            fab.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView2, AttendanceFragment())
                    .commit()
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
    }



    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}