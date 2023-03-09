package com.example.dsmabsen.ui.fragment

import android.Manifest.permission.*
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.dsmabsen.databinding.FragmentAttendanceBinding
import com.example.dsmabsen.helper.TokenManager
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.AttendanceViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import javax.inject.Inject

@AndroidEntryPoint
class AttendanceFragment :
    BaseFragment<FragmentAttendanceBinding>(FragmentAttendanceBinding::inflate) {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private val viewModel: AttendanceViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val savedUser = Paper.book().read<DataX>("user")
            viewModel.attendanceTotalRequest(savedUser!!.nip)
            viewModel.totalAttendanceLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val attendance = it.data!!.data

                    }

                    is NetworkResult.Loading -> {

                    }

                    is NetworkResult.Error -> {

                    }
                }
            }

            locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationListener = LocationListener { location ->
                val latitude = location.latitude
                val longitude = location.longitude
                textView42.text = latitude.toString()
                textView43.text = longitude.toString()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val permission =
            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, ACCESS_BACKGROUND_LOCATION)
        permissionMultiRequest.launch(permission)
    }

    private val permissionMultiRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            var areAllGranted = true
            for (isGranted in result.values) {
                Toast.makeText(requireContext(), "Granted is $isGranted", Toast.LENGTH_SHORT).show()
                areAllGranted = areAllGranted && isGranted
            }

            if (areAllGranted) {
//                Toast.makeText(
//                    requireContext(),
//                    "Permintaan Izin di aktifkan",
//                    Toast.LENGTH_SHORT
//                ).show()
                getLocationUser()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permintaan Izin di nonaktifkan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun getLocationUser() {
//        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)

    }

    override fun onStop() {
        super.onStop()
        locationManager
    }



//    private fun requestLocationUpdates(
//        gpsProvider: String,
//        i: Int,
//        fl: Float,
//        attendanceFragment: AttendanceFragment
//    ) {
//
//    }

}
