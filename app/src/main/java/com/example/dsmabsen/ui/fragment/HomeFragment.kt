package com.example.dsmabsen.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentHomeBinding
import com.example.dsmabsen.helper.Constans
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val LOCATION_PERMISSION_CODE = 123
    private val MY_PERMISSIONS_REQUEST_CAMERA = 456

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
            checkForPermission(
                Manifest.permission.CAMERA,
                "camera",
                MY_PERMISSIONS_REQUEST_CAMERA
            )

            checkForPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                "location",
                LOCATION_PERMISSION_CODE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            } else {
                val permission = Manifest.permission.READ_PHONE_STATE
                singlePermissionLaunch.launch(permission)
            }

//camera >= M kasih izin
//            Lokasi
//Imei android dibawah 10 izin imei


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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "izin $name ditolak", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "izin $name disetujui", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (requestCode) {
            LOCATION_PERMISSION_CODE -> innerCheck("location")
            MY_PERMISSIONS_REQUEST_CAMERA -> innerCheck("camera")
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage("membutuhkan izin $name untuk menggunakan aplikasi ini")
            setTitle("Membutuhkan izin")
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

    private val singlePermissionLaunch =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Log.d(Constans.TAG, "Membutuhkan izin: $isGranted")
            if (isGranted) {
                Toast.makeText(requireContext(), "Izin disetujui", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "izin ditolak ", Toast.LENGTH_SHORT).show()
            }
        }


}