package com.example.dsmabsen.ui.activity

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.ActivityMainBinding
import com.example.dsmabsen.databinding.CustomUbahPasswordBinding
import com.example.dsmabsen.helper.ConnectionLiveData
import com.example.dsmabsen.helper.TokenManager
import com.example.dsmabsen.helper.handleApiErrorActivity
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.PasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    Todo Gone lek

    @Inject
    lateinit var tokenManager: TokenManager

    private val viewModel: PasswordViewModel by viewModels()
    lateinit var navController: NavController
    private lateinit var cld: ConnectionLiveData
    lateinit var binding: ActivityMainBinding
    val savedUser = Paper.book().read<DataX>("user")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkNetworkConnection()
        checkPassword(savedUser)
        showBottomNav(savedUser)

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

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//
//            val arrayHideNavbar = arrayOf(
//                R.id.berandaFragment,
//                R.id.attendanceFragment,
//                R.id.dataAbsenFragment,
//                R.id.profileFragment,
//                R.id.visitFragment2
//            )
//            val arrayFragmentBgWhite = arrayOf(
//                R.id.allProfileFragment,
//                R.id.personalDataFragment,
//                R.id.penguasaanBahasaFragment,
//                R.id.jabatanDanPosisiFragment,
//                R.id.lokasiKerjaFragment,
//                R.id.pendidikanFragment,
//                R.id.pengalamanKerjaFragment,
//                R.id.penganjuanVisitFragment
//            )

//            if (arrayHideNavbar.contains(destination.id)) {
//                binding.bottomNavigationView.visibility = View.VISIBLE
//            } else {
//                binding.bottomNavigationView.visibility = View.GONE
//
//            }
//            if (arrayFragmentBgWhite.contains(destination.id)) {
//                binding.main.setBackgroundColor(Color.WHITE)
//
//            }
//            else {
//                binding.main.background = AppCompatResources.getDrawable(
//                    applicationContext,
//                    R.drawable.gradiend_background
//                )
//            }
//        }


    }

    private fun checkPassword(savedUser: DataX?) {
        viewModel.passwordCheckRequest(savedUser!!.nip)
        viewModel.passwordCheckLiveData.observe(this) { it ->
            when (it) {
                is NetworkResult.Success -> {
                    val response = it.data!!
                    val status = response.data.status
                    if (status == 0) {
                        val dialogBinding =
                            CustomUbahPasswordBinding.inflate(LayoutInflater.from(this))

                        val builder = AlertDialog.Builder(this)
                        builder
                            .setTitle("Ubah Password")
                            .setView(dialogBinding.root)
                            .setCancelable(false)
                            .setPositiveButton("Ubah") { dialog, which ->


                            }


                        // Show the dialog
                        val dialog = builder.create()
                        dialog.show()
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                            val oldPassword = dialogBinding.etOldPassword.text.toString()
                            val newPassword = dialogBinding.etNewPassword.text.toString()
                            if (oldPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                                val messages = "Berhasil"
                                if(oldPassword == newPassword ){
                                    dialogBinding.errorText.text = "Password lama dan baru tidak boleh sama"
                                }else{
                                    dialogBinding.errorText.text = messages
                                    dialogBinding.errorText.visibility = View.VISIBLE
                                    viewModel.ubahPasswordRequest(savedUser!!.nip, oldPassword, newPassword)

                                    dialog.dismiss()
                                }
                            } else {
                                val messages = "Isi semua kolom password terlebih dahulu"
                                dialogBinding.errorText.text = messages
                                dialogBinding.errorText.visibility = View.VISIBLE
                            }
                        }
                    }
                }


                is NetworkResult.Loading -> {

                }

                is NetworkResult.Error -> {
                    Log.d("TAG", it.message.toString())
                    handleApiErrorActivity(it.message)
                }
            }
        }
    }

    private fun showBottomNav(visit: DataX?) {
        val visited = visit!!.eselon
        if (visited > "3") {
            binding.bottomNavigationView.menu.removeItem(R.id.visitFragment2)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun checkNetworkConnection() {
        cld = ConnectionLiveData(application)

        cld.observe(this) { isConnected ->
            if (isConnected) {
                binding.fragmentContainerView2.visibility = View.VISIBLE
                binding.bottomNavigationView.visibility = View.VISIBLE
                binding.noInternetConnection.ivNoConnection.visibility = View.GONE
            } else {
                binding.fragmentContainerView2.visibility = View.GONE
                binding.bottomNavigationView.visibility = View.GONE
                binding.noInternetConnection.ivNoConnection.visibility = View.VISIBLE
            }

        }
    }
}