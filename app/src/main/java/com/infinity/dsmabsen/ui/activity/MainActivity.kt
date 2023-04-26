package com.infinity.dsmabsen.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ActivityMainBinding
import com.infinity.dsmabsen.databinding.CustomUbahPasswordBinding
import com.infinity.dsmabsen.helper.ConnectionLiveData
import com.infinity.dsmabsen.helper.TokenManager
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.helper.handleApiErrorActivity
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.fragment.BerandaFragmentDirections
import com.infinity.dsmabsen.ui.viewModel.PasswordViewModel
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
//                Log.d("Destination", "bottom nav show")
                when (destination.id) {
                    R.id.homeFragment -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                        Log.d("in beranda", "")
                    }
                    R.id.visitFragment2 -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                        Log.d("in in visit", "")
                    }
                    R.id.attendanceFragment -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                        Log.d("in attendance", "")
                    }
                    R.id.dataAbsenFragment -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                        Log.d("in data absen", "")
                    }
                    R.id.profileFragment -> {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                        Log.d("in profile", "")
                    }
                    else -> {
                        binding.bottomNavigationView.visibility = View.GONE
                    }
                }
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
    }

    private fun checkPassword(savedUser: DataX?) {
        viewModel.passwordCheckRequest(savedUser!!.nip)
        viewModel.passwordCheckLiveData.observe(this) {
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
                            val loadings = dialogBinding.loadings.loading
                            if (oldPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                                if (oldPassword == newPassword) {
                                    dialogBinding.errorText.text =
                                        "Password lama dan baru tidak boleh sama"
                                    dialogBinding.errorText.visibility =
                                        View.VISIBLE
                                } else {
                                    viewModel.ubahPasswordRequest(
                                        savedUser!!.nip,
                                        oldPassword, newPassword
                                    )
                                    viewModel.ubahPasswordLiveData.observe(this) { ubahPassword ->
                                        loadings.visibility = View.GONE
                                        when (ubahPassword) {
                                            is NetworkResult.Success -> {
                                                val responses = ubahPassword.data!!
                                                val message = responses.data.message
                                                val statuses = responses.data.status

                                                if (statuses == 0) {
                                                    loadings.visibility = View.GONE
                                                    dialogBinding.errorText.visibility =
                                                        View.VISIBLE
                                                    dialogBinding.errorText.text = message
                                                } else {
                                                    dialog.dismiss()
                                                    Toast.makeText(
                                                        this,
                                                        message,
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                }
                                            }

                                            is NetworkResult.Loading -> {
                                                loadings.visibility = View.VISIBLE
                                            }

                                            is NetworkResult.Error -> {
                                                handleApiErrorActivity(ubahPassword.message)
                                                loadings.visibility = View.GONE
                                            }
                                        }
                                    }
//                                val messages = "Berhasil"
//                                if (oldPassword == newPassword) {
//                                    dialogBinding.errorText.text =
//                                        "Password lama dan baru tidak boleh sama"
//                                } else {
//                                    dialogBinding.errorText.text = messages
//                                    dialogBinding.errorText.visibility = View.VISIBLE
//                                    viewModel.ubahPasswordRequest(
//                                        savedUser!!.nip,
//                                        oldPassword,
//                                        newPassword
//                                    )
//
//                                    dialog.dismiss()
//                                }
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
        if (visited < "3") {
            binding.bottomNavigationView.menu.removeItem(R.id.visitFragment2)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun checkNetworkConnection() {
        cld = ConnectionLiveData(application)

        cld.observe(this) { isConnected ->
//            navController.addOnDestinationChangedListener { _, destination, _ ->

            if (isConnected) {
                Log.d("is connect", "bottom nav show")
                binding.fragmentContainerView2.visibility = View.VISIBLE
                binding.noInternetConnection.ivNoConnection.visibility = View.GONE
//                    binding.bottomNavigationView.visibility = View.VISIBLE
//                        if (destination.id == R.id.berandaFragment) {
//                            binding.bottomNavigationView.visibility = View.VISIBLE
//                            Log.d("fragment", "sekarang diberanda")
//                        }

//                        if (destination.id == R.id.homeFragment ||
//                            destination.id == R.id.visitFragment2 ||
//                            destination.id == R.id.attendanceFragment ||
//                            destination.id == R.id.dataAbsenFragment ||
//                            destination.id == R.id.profileFragment
//                        ) {
//                            binding.bottomNavigationView.visibility = View.VISIBLE
//                        } else {
//                            binding.bottomNavigationView.visibility = View.VISIBLE
//                        }
            } else {
                Log.d("no connect ", "bottom nav hide")
                binding.fragmentContainerView2.visibility = View.GONE
                binding.bottomNavigationView.visibility = View.GONE
                binding.noInternetConnection.ivNoConnection.visibility = View.VISIBLE
            }
//            }
        }
    }

    fun toast() {
        Toast.makeText(this, "fauzan", Toast.LENGTH_SHORT).show()
    }

    fun hideMyBottomNav() {
        Log.d("BN", "HIDE IN MAIN")
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showMyBottomNav() {
        Log.d("BN", "SHOW IN MAIN")
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

}