package com.infinity.dsmabsen.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentHomeBinding
import com.infinity.dsmabsen.ui.activity.LoginActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )
    private val ALL_PERMISSIONS_CODE = 1
    private val PERMISSION_DENIED_PERMANENTLY_CODE = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.requestPermissionsButton.setOnClickListener {
            if (!checkAllPermissionsGranted(permissions)) {
                requestPermissions(permissions, ALL_PERMISSIONS_CODE)
            } else {
                startLoginActivity()
            }
        }
        if (!checkAllPermissionsGranted(permissions)) {
            requestPermissions(permissions, ALL_PERMISSIONS_CODE)
        } else {
            startLoginActivity()
        }
    }

    private fun checkAllPermissionsGranted(permissions: Array<out String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
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

        when (requestCode) {
            ALL_PERMISSIONS_CODE -> {
                if (checkAllPermissionsGranted(permissions)) {
                    startLoginActivity()
                } else {
                    // check if any permission was denied permanently
                    var isAnyPermissionDeniedPermanently = false
                    for (i in permissions.indices) {
                        if (!shouldShowRequestPermissionRationale(permissions[i]) &&
                            grantResults[i] != PackageManager.PERMISSION_GRANTED
                        ) {
                            isAnyPermissionDeniedPermanently = true
                            break
                        }
                    }
                    if (isAnyPermissionDeniedPermanently) {
                        // show an explanation dialog to the user
                        showExplanationDialog(PERMISSION_DENIED_PERMANENTLY_CODE)
                    } else {
                        // show rationale and request permissions again
                        showExplanationDialog(ALL_PERMISSIONS_CODE)
                    }
                }
            }
            PERMISSION_DENIED_PERMANENTLY_CODE -> {
                // open device settings so that user can enable the permissions
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }

    private fun showExplanationDialog(requestCode: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("aplikasi membutuhkan perizinan")
        builder.setMessage("aplikasi membutuhakn beberapa perizinn agar dapat digunakan")

        builder.setPositiveButton("buka pengaturan") { _, _ ->
            if (requestCode == PERMISSION_DENIED_PERMANENTLY_CODE) {
                // open device settings so that user can enable the permissions
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            } else {
                // show rationale and request permissions again
                requestPermissions(permissions, ALL_PERMISSIONS_CODE)
            }
        }

        builder.setNegativeButton("batal") { _, _ ->
            Toast.makeText(
                requireContext(),
                "perizinan dibatalkan",
                Toast.LENGTH_SHORT
            ).show()
        }

        builder.show()
    }

    private fun startLoginActivity() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }
}

