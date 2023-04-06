package com.infinity.dsmabsen.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.infinity.dsmabsen.databinding.FragmentHomeBinding
import com.infinity.dsmabsen.ui.activity.LoginActivity

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding


    companion object {
        const val ALL_PERMISSIONS_CODE = 1
    }

    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        // Check for all permissions
        if (!checkAllPermissionsGranted(permissions)) {
            // Show the "Request Permissions" button
            binding.requestPermissionsButton.visibility = View.VISIBLE

            // Set button click listener
            binding.requestPermissionsButton.setOnClickListener {
                // Request the permissions
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissions,
                    ALL_PERMISSIONS_CODE
                )
            }
        } else {
            // Hide the "Request Permissions" button
            binding.requestPermissionsButton.visibility = View.GONE

            startLoginActivity()
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            if (checkAllPermissionsGranted(permissions)) {
                requestPermissionsButton.visibility = View.GONE
            } else {
                requestPermissionsButton.visibility = View.VISIBLE
                requestPermissionsButton.setOnClickListener {
                    requestPermissions(permissions, ALL_PERMISSIONS_CODE)
                }
            }
        }
    }

    private fun checkAllPermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == ALL_PERMISSIONS_CODE) {
            var allGranted = true
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false
                    break
                }
            }
            if (allGranted) {
                // Hide the "Request Permissions" button
                binding.requestPermissionsButton.visibility = View.GONE

                startLoginActivity()
            } else {
                Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startLoginActivity() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }
}
