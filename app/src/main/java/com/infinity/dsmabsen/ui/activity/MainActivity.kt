package com.infinity.dsmabsen.ui.activity

import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ActivityMainBinding
import com.infinity.dsmabsen.databinding.CustomUbahPasswordBinding
import com.infinity.dsmabsen.databinding.LayoutWarningDailogBinding
import com.infinity.dsmabsen.helper.ConnectionLiveData
import com.infinity.dsmabsen.helper.TokenManager
import com.infinity.dsmabsen.helper.handleApiErrorActivity
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.viewModel.PasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    Todo Gone lek

    @Inject
    lateinit var tokenManager: TokenManager
    private var alertDialog: AlertDialog? = null
    private val viewModel: PasswordViewModel by viewModels()
    lateinit var navController: NavController
    private lateinit var cld: ConnectionLiveData
    lateinit var binding: ActivityMainBinding
    val savedUser = Paper.book().read<DataX>("user")
    val nip = savedUser!!.nip
    val eselon = savedUser!!.eselon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkNetworkConnection()
        checkPassword()
        showBottomNav()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.findNavController()

//        Bottom Navigation
        with(binding) {
            bottomNavigationView.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
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

            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (navController.currentDestination?.id == R.id.berandaFragment) {
                        val dialogBinding = LayoutWarningDailogBinding.inflate(layoutInflater)

                        val alertDialog =
                            AlertDialog.Builder(this@MainActivity, R.style.AlertDialogTheme)
                                .setView(dialogBinding.root)
                                .create()

                        dialogBinding.textTitle.text = "Konfirmasi Keluar"
                        dialogBinding.textMessage.text =
                            "Anda yakin Ingin keluar dari this aplikasi"
                        dialogBinding.buttonYes.text = "Ya"
                        dialogBinding.buttonNo.text = "Batal"
                        dialogBinding.imageIcon.setImageResource(R.drawable.ic_baseline_warning_24)

                        dialogBinding.buttonYes.setOnClickListener {
                            alertDialog.dismiss()
                            finish()

                        }
                        dialogBinding.buttonNo.setOnClickListener {
                            alertDialog.dismiss()
                        }

                        if (alertDialog.window != null) {
                            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
                        }

                        alertDialog.show()
                    } else {
                        navController.navigateUp()
                    }
                }
            }


        }
    }

    override fun onDestroy() {
        alertDialog?.dismiss()
        super.onDestroy()
    }

    private fun checkPassword() {
        viewModel.passwordCheckRequest(nip)
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
                            .setPositiveButton("Ubah") { _, _ ->
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
                                        nip,
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

    private fun showBottomNav() {
        if (eselon < "3") {
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
                Log.d("is connect", "bottom nav show")
                binding.fragmentContainerView2.visibility = View.VISIBLE
                binding.noInternetConnection.ivNoConnection.visibility = View.GONE
                binding.bottomNavigationView.visibility = View.VISIBLE
            } else {
                Log.d("no connect ", "bottom nav hide")
                binding.fragmentContainerView2.visibility = View.GONE
                binding.bottomNavigationView.visibility = View.GONE
                binding.noInternetConnection.ivNoConnection.visibility = View.VISIBLE
            }
        }
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