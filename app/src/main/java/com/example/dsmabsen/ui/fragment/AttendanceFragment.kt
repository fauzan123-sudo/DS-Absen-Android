package com.example.dsmabsen.ui.fragment

import android.Manifest.permission.*
import android.app.Activity
import android.util.Base64
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.dsmabsen.databinding.FragmentAttendanceBinding
import com.example.dsmabsen.helper.Constans
import com.example.dsmabsen.helper.TokenManager
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.AttendanceViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class AttendanceFragment :
    BaseFragment<FragmentAttendanceBinding>(FragmentAttendanceBinding::inflate) {
    private var selectedImageUri: Uri? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    //    lateinit var latittude: String
//    lateinit var longitude2: String
    private val viewModel: AttendanceViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager
    val savedUser = Paper.book().read<DataX>("user")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {


//            val image: MultipartBody.Part =
//            val date = ""
//            val timeZone = ""

            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            val location = fusedLocationProviderClient.lastLocation
            location.addOnSuccessListener {
                if (it != null) {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    textView42.text = latitude.toString()
                    textView43.text = longitude.toString()
                }
            }





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
//                latittude = location.latitude.toString()
//                longitude2 = location.longitude.toString()
                textView42.text = latitude.toString()
                textView43.text = longitude.toString()
            }

            imageView23.setOnClickListener {
                camera()
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


    private fun camera() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkCameraPermission()
//        }else {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
        putPhoto.launch(intent)
//        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            val permission = CAMERA
            singlePermissionLaunch.launch(permission)
        }
    }

    private val putPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                val selectedImageUris = it?.data?.extras?.get("data") as Bitmap
                val uri = it?.data?.data
                absen(selectedImageUris!!)

                binding.photo.setImageBitmap(selectedImageUris)            /* imgPhoto.load(bitmap) */
                binding.photo.isVisible = true


            } else if (it == null) {
                Toast.makeText(requireContext(), "Gambar tida dapat di set", Toast.LENGTH_SHORT)
                    .show()
//            imgPhoto.setImageResource(R.drawable.ic_launcher_background)
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }

        }

    private fun absen(uri: Bitmap) {

        val photo = uriToMultipartBody(uri)


        Log.d("gambar", photo.toString())

        val nipRequestBody = MultipartBody.Part.createFormData("nip", savedUser!!.nip)
        val dateRequestBody = MultipartBody.Part.createFormData("date", "2023-03-10 11:41:00")
        val timezoneRequestBody = MultipartBody.Part.createFormData("timezone", "")
        val kordinatRequestBody = MultipartBody.Part.createFormData("kordinat", "123, 456")
        val kodeShiftRequestBody = MultipartBody.Part.createFormData("kode_shift", "12")
        val kodeTingkatRequestBody = MultipartBody.Part.createFormData("kode_tingkat", "0")

        viewModel.attendanceToday(
            photo,
            nipRequestBody.body,
            dateRequestBody.body,
            timezoneRequestBody.body,
            kordinatRequestBody.body,
            kodeShiftRequestBody.body,
            kodeTingkatRequestBody.body
        )
        viewModel.attendanceTodayLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (it.data!!.status) {
                        Toast.makeText(requireContext(), "success upload file", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), "error upload file", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is NetworkResult.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Error -> {
                    handleApiError(it.message)
                }
            }
        }

    }

    private fun uriToMultipartBody(bitmap: Bitmap): MultipartBody.Part {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP)
        val requestFile = encodedImage.toRequestBody("image/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", "image.png", requestFile)

    }


    private val singlePermissionLaunch =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Log.d(Constans.TAG, "permission is Granted: $isGranted")
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission camera is granted", Toast.LENGTH_SHORT)
                    .show()

            } else {
                Log.d(Constans.TAG, "Permission camera  : Permission Denied ")
                Toast.makeText(requireContext(), "Permission camera Denied ", Toast.LENGTH_SHORT)
                    .show()
            }
        }

}
