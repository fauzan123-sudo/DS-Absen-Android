package com.example.dsmabsen.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.dsmabsen.databinding.ActivityLoginBinding
import com.example.dsmabsen.helper.ConnectionLiveData
import com.example.dsmabsen.helper.Constans.TAG
import com.example.dsmabsen.helper.TokenManager
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.helper.handleApiErrorActivity
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var imei: String
    private lateinit var telephonyManager: TelephonyManager
    private lateinit var cld: ConnectionLiveData

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var binding: ActivityLoginBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        checkNetworkConnection()



        if (tokenManager.getToken() != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val mId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        } else {
            val permission = Manifest.permission.READ_PHONE_STATE
            singlePermissionLaunch.launch(permission)
        }

        with(binding) {
            btnLogin.setOnClickListener {
                val myUserName = nip.text.toString()
                val myPassword = password.text.toString()
                when {
                    myUserName.isEmpty() -> {
                        nip.error = "harap isi nip"
                        nip.requestFocus()
                    }
                    myPassword.isEmpty() -> {
                        password.error = "harap isi passwordnya!!"
                        password.requestFocus()
                    }
                    else -> {
                        constrain.isVisible = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            viewModel.login(myUserName, myPassword, mId)
                        } else {
                            viewModel.login(myUserName, myPassword, imei)
                        }
                    }
                }
            }

            viewModel.userResponseLiveData.observe(this@LoginActivity) {
                loadingInclude.loading.isVisible = false
                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data!!.status) {
                            tokenManager.saveToken(it.data.access_token)

                            val data = it.data.data
                            saveDataNip(data)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            val message = it.data!!.message
                            constrain.isVisible = true
                            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    is NetworkResult.Error -> {
                        constrain.isVisible = true
                        val error = it.message.toString()
                        handleApiErrorActivity(error)
                        Log.d(TAG, (it.message.toString()))
                    }

                    is NetworkResult.Loading -> {
                        binding.loadingInclude.loading.isVisible = true
                    }
                }
            }
        }
    }

    private fun checkNetworkConnection() {
        cld = ConnectionLiveData(application)

        cld.observe(this) { isConnected ->
            if (isConnected) {
//                layout1.visibility = View.VISIBLE
//                layout2.visibility = View.GONE
            } else {
//                layout1.visibility = View.GONE
//                layout2.visibility = View.VISIBLE
            }

        }
    }

    private fun saveDataNip(data: DataX) {
        Paper.book().write("user", data)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private val singlePermissionLaunch =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Log.d(TAG, "permission is Granted: $isGranted")
            if (isGranted) {
                Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show()
                imei = telephonyManager.imei
                Toast.makeText(this, imei, Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "Permission Single : Permission Denied ")
                Toast.makeText(this, "Permission Denied ", Toast.LENGTH_SHORT).show()
            }
        }
}