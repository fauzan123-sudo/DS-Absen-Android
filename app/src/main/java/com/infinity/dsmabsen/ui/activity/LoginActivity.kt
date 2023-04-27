package com.infinity.dsmabsen.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.infinity.dsmabsen.databinding.ActivityLoginBinding
import com.infinity.dsmabsen.helper.AlertDialogHelper
import com.infinity.dsmabsen.helper.ConnectionLiveData
import com.infinity.dsmabsen.helper.Constans.TAG
import com.infinity.dsmabsen.helper.TokenManager
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()

    private lateinit var telephonyManager: TelephonyManager
    private lateinit var cld: ConnectionLiveData

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var binding: ActivityLoginBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.loadingInclude.loading.visibility = View.GONE
        setContentView(binding.root)
        val rootView = binding.root
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
                        Log.d(TAG, "else button")
                        viewModel.login(myUserName, myPassword, mId)
                    }
                }
            }

            viewModel.userResponseLiveData.observe(this@LoginActivity) {
                loadingInclude.loading.visibility = View.GONE
                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data!!.status) {
                            tokenManager.saveToken(it.data.access_token)

                            val data = it.data.data
                            saveDataNip(data)
                            Log.d("data user", "$data")
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            val message = it.data!!.message
                            constrain.isVisible = true
                            val alertDialogHelper = AlertDialogHelper(this@LoginActivity)
                            alertDialogHelper.showAlertDialog("", message)
                        }
                        binding.loadingInclude.loading.visibility = View.GONE

                    }
                    is NetworkResult.Error -> {
                        constrain.isVisible = true
//                        handleApiErrorActivity(error)
                        Snackbar.make(rootView, it.message!!, Snackbar.LENGTH_SHORT).show()
                        Log.d("login error response", (it.message.toString()))
                        binding.loadingInclude.loading.visibility = View.GONE

                    }

                    is NetworkResult.Loading -> {
                        Log.d("login", "loading .... ")
                        binding.loadingInclude.loading.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

//    private fun getIMEI(context: Context): String {
//        var imei: String? = ""
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                val telephonyManager =
//                    context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//                imei = telephonyManager.imei
//                return if (imei.isNullOrEmpty()) {
//                    "IMEI tidak ditemukan"
//                } else {
//                    imei
//                }
//            }
//        } else {
//            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                val subscriptionManager =
//                    context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
//                val subscriptionInfoList = subscriptionManager.activeSubscriptionInfoList
//                if (subscriptionInfoList != null && subscriptionInfoList.isNotEmpty()) {
//                    val subscriptionInfo = subscriptionInfoList[0]
//                    imei = subscriptionInfo?.iccId
//                    return if (imei.isNullOrEmpty()) {
//                        "Nomor IMEI tidak ditemukan"
//                    } else {
//                        imei
//                    }
//                }
//            }
//        }
//        return imei!!
//    }


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
                val imei = telephonyManager.imei
                if (imei != null) {
                    Log.d(TAG, imei)
                } else {
                    Log.d(TAG, "Nomor IMEI tidak ditemukan")
                }
            } else {
                Log.d(TAG, "Permission Single : Permission Denied ")
            }
        }

}