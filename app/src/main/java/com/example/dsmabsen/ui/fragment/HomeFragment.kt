package com.example.dsmabsen.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.dsmabsen.databinding.FragmentHomeBinding
import com.example.dsmabsen.ui.activity.LoginActivity

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val LOCATION_PERMISSION_CODE = 123 // kode permintaan izin lokasi
    private val MY_PERMISSIONS_REQUEST_CAMERA = 456 // kode permintaan izin kamera

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

//            checkForPermission(
//                Manifest.permission.CAMERA,
//                "camera",
//                MY_PERMISSIONS_REQUEST_CAMERA
//            )
//
//            checkForPermission(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                "location",
//                LOCATION_PERMISSION_CODE
//            )

            fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
                ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
            val _PERMISSION_ALL = 1
            val _PERMISSIONS = arrayOf(

                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,

            )

            if (!hasPermissions(requireContext(), *_PERMISSIONS)) {
                ActivityCompat.requestPermissions(requireContext() as Activity, _PERMISSIONS, _PERMISSION_ALL)
//                if (ActivityCompat.checkSelfPermission(requireContext(), _PERMISSIONS.toString())
//                    == PackageManager.PERMISSION_GRANTED ){
//                    startActivity(Intent(requireContext(), LoginActivity::class.java))
//                }
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            } else {
                Toast.makeText(requireContext(), "Required Permission", Toast.LENGTH_SHORT).show()
            }

            imageView13.setOnClickListener {
                checkForPermission(
                    Manifest.permission.CAMERA,
                    "camera",
                    MY_PERMISSIONS_REQUEST_CAMERA
                )
            }

            textView39.setOnClickListener {
                checkForPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    "location",
                    LOCATION_PERMISSION_CODE
                )
            }
        }
    }

    private fun checkForPermission(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    Toast.makeText(requireContext(), "$name permission granted", Toast.LENGTH_SHORT)
                        .show()
                }

                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission,
                    name,
                    requestCode
                )
                else -> ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            }
        }
    }

    fun cameraPermission(){
        ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "$name Permission refused ", Toast.LENGTH_SHORT)
                    .show()
            } else {
                MoveLogin()
                Toast.makeText(requireContext(), "$name Permission granted ", Toast.LENGTH_SHORT)
                    .show()

            }
        }

        when (requestCode) {
            LOCATION_PERMISSION_CODE -> innerCheck("location")
            MY_PERMISSIONS_REQUEST_CAMERA -> innerCheck("camera")
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun MoveLogin() {
        val ss = Intent(requireContext(), LoginActivity::class.java)
        ss.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        ss.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ss.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        ss.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(ss)
    }
    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { dialog, whisch ->
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            }
        }

        val dialog = builder.create()
        dialog.show()
    }


}