package com.infinity.dsmabsen.ui.fragment

import android.Manifest.permission.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentAttendanceBinding
import com.infinity.dsmabsen.helper.*
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.viewModel.AttendanceViewModel
import com.infinity.dsmabsen.ui.viewModel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AttendanceFragment :
    BasesFragment<FragmentAttendanceBinding>(FragmentAttendanceBinding::inflate) {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private val viewModel: AttendanceViewModel by viewModels()
    private val handler = Handler()
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private lateinit var dialog: AlertDialog

    private lateinit var progressDialog :SweetAlertDialog

    var latittudeUser1: String? = null
    var longitudeUser2: String? = null
    private lateinit var runnable: Runnable

    private val savedUser = Paper.book().read<DataX>("user")
    val nip = savedUser!!.nip

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
        timeRun()

        binding.apply {
            btnAbsen.setOnClickListener {
                camera()
            }
            btnScanWajah.setOnClickListener {
                findNavController().navigate(R.id.action_attendanceFragment_to_faceScanningFragment3)
            }
            btnRefresh.setOnClickListener {
                if (!isGpsEnabled()) {
                    dialog.show()
                }
                getLocation()
            }
            getProfileUser()
            requestLocationPermission()
            locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            dialog = AlertDialog.Builder(requireContext())
                .setMessage("GPS tidak aktif, silakan aktifkan GPS.")
                .setPositiveButton("Pengaturan") { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton("Batal") { _, _ ->
                    gpsNoActive()
                }
                .setCancelable(false)
                .create()

            if (!isGpsEnabled()) {
                dialog.show()
            }


        }
    }

    private fun isGpsEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun gpsNoActive() {
        disableAbsen()
    }

    private fun getProfileUser() {
        userProfileViewModel.profileUserRequest(nip)
        userProfileViewModel.profileUserLivedata.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    binding.apply {
                        val response = it.data!!
                        val status = response.status
                        val imageUser = response.data.foto
                        toolbarUser.namaUser.text = response.data.nama
                        toolbarUser.jabatan.text = response.data.jabatan
                        if (status) {
                            Glide.with(requireContext())
                                .load(imageUser)
                                .circleCrop()
                                .placeholder(R.drawable.user)
                                .into(toolbarUser.imageUser)
                        }
                    }
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Error -> {
                    handleApiError(it.message)
                }
            }
        }
    }

    private fun getLocationUser() {
        runLoading()
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            stopLoading()
            if (it != null) {
                with(binding) {
                    latittudeUser1 = it.latitude.toString()
                    longitudeUser2 = it.longitude.toString()
                    lattitudeUser.text = it.latitude.toString()
                    longitudeUser.text = it.longitude.toString()
                }
                enableAbsen()
            } else {
                with(binding) {
                    lattitudeUser.text = "-"
                    longitudeUser.text = "-"
                }
                disableAbsen()
            }
            Log.d("ambil_lokasi", "On3 Proses")
            getAddressAboveSDK29()
        }
    }

    private fun getLocation() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            locationListener
        )
        Log.d("ambil_lokasi", "On Proses")
        getLocationUser()
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            stopLoading()
            val latitude = location.latitude
            val longitude = location.longitude
            if (location != null) {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        val address = addresses[0].getAddressLine(0)
                        binding.currentLocation.text = address.toString()

                    } else {
                        binding.currentLocation.text = "-"
                        disableAbsen()
                        handleApiError("Lokasi tidak ditemukan")
                    }

                    binding.lattitudeUser.text = latitude.toString()
                    binding.longitudeUser.text = longitude.toString()
                } catch (e: Exception) {
                    handleApiError(e.toString())
                }

            } else {
                runLoading()
                handleApiError("lokasi tidak dapat ditemukan")
            }

            if (isGpsEnabled()) {
                dialog.dismiss()
                enableAbsen()
            } else {
                runLoading()
            }
        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onProviderDisabled(provider: String) {
            dialog.show()
            runLoading()
        }
    }

    override fun onStart() {
        super.onStart()
        getLocation()
    }

    override fun onStop() {
        super.onStop()
        locationManager
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 0)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            locationListener
        )
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
        locationManager.removeUpdates(locationListener)
    }

    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            val manager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
            var cameraId = ""
            for (id in manager.cameraIdList) {
                val characteristics = manager.getCameraCharacteristics(id)
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    cameraId = id
                    break
                }
            }
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1)
            intent.putExtra("android.intent.extras.CAMERA_LENS_FACING_FRONT", 1)
            intent.putExtra("camera_id", cameraId)
            putPhoto.launch(intent)
        }
    }

    private fun absen(uri: Bitmap) {
        val photo = uriToMultipartBody(uri)
        Log.d("gambar", photo.toString())
        val timeNow = getTime()
        val nipRequestBody = MultipartBody.Part.createFormData("nip", nip)
        val dateRequestBody = MultipartBody.Part.createFormData("date", timeNow)
        val timezoneRequestBody = MultipartBody.Part.createFormData("timezone", "")
        val kordinatRequestBody =
            MultipartBody.Part.createFormData("kordinat", "$latittudeUser1,$longitudeUser2")
        val kodeShiftRequestBody =
            MultipartBody.Part.createFormData("kode_shift", savedUser?.shift ?: "")
        val kodeTingkatRequestBody = MultipartBody.Part.createFormData("kode_tingkat", "0")

        Log.d(
            "TAG",
            "nip :$nipRequestBody date:$dateRequestBody timeZone:$timezoneRequestBody kordinat:$latittudeUser1,$longitudeUser2 kodeshift:$kodeShiftRequestBody kodetingkat:$kodeTingkatRequestBody"
        )

        viewModel.attendanceToday2(
            photo,
            nipRequestBody.body,
            dateRequestBody.body,
            timezoneRequestBody.body,
            kordinatRequestBody.body,
            kodeShiftRequestBody.body,
            kodeTingkatRequestBody.body
        )
        viewModel.presensiLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {


                    val response = it.data!!
                    val statusPresensi = response.data.status
                    val message = response.data.messages
                    if (statusPresensi == "Error") {
                        progressDialog.dismiss()
                        val sweetAlertDialog =
                            SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        sweetAlertDialog.titleText = ""
                        sweetAlertDialog.contentText = message
                        sweetAlertDialog.show()
                        binding.apply {
                            materialCardView21.isVisible = true
                            notificationUser.isVisible = true
                            informationLayout.isVisible = true
                            notificationUser.text = message
                            informationLayout.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color._danger
                                )
                            )
                        }
                    } else {
                        binding.apply {
                            binding.materialCardView21.isVisible = true
                            binding.notificationUser.isVisible = true
                            binding.informationLayout.isVisible = true
                            binding.notificationUser.text = message
                            binding.informationLayout.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color._success
                                )
                            )
                        }
                    }

                }
                is NetworkResult.Loading -> {
                    progressDialog.progressHelper.barColor =
                        ContextCompat.getColor(requireContext(), R.color._primary)
                    progressDialog.titleText = "Loading"
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                }

                is NetworkResult.Error -> {
                    progressDialog.dismiss()
                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText(it.message)
                        .show()
                    handleApiError(it.message)
                }
            }
        }


    }

    private val putPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedImageUris = it?.data?.extras?.get("data") as Bitmap
                absen(selectedImageUris!!)

            } else if (it == null) {
                Toast.makeText(requireContext(), "Gambar tidak dapat di set", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Log.d("TAG", "Task Cancelled")
            }

        }

    private fun uriToMultipartBody(bitmap: Bitmap): MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val requestFile = byteArray.toRequestBody("image/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", "image.png", requestFile)
    }

    private fun getTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    private fun getAddressAboveSDK29() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) Log.d("ambil_lokasi", "On4 Proses")
        runLoading()

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                try {
                    val provider = LocationManager.NETWORK_PROVIDER
                    val location = locationManager.getLastKnownLocation(provider)
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val alertDialogHelper = AlertDialogHelper(requireContext())
                        try {
                            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                            if (addresses != null && addresses.isNotEmpty()) {
                                val address = addresses[0].getAddressLine(0)
                                binding.currentLocation.text = address
//                                enableAbsen()
                            } else {
                                binding.currentLocation.text = "-"
//                                disableAbsen()
                                alertDialogHelper.showAlertDialog(
                                    "Absen",
                                    "Lokasi tidak di temukan"
                                )
                            }
                        } catch (e: IOException) {
                            binding.currentLocation.text = "-"
//                            disableAbsen()
                            alertDialogHelper.showAlertDialog(
                                "Absen",
                                "Lokasi tidak di temukan, periksa kembali koneksi anda"
                            )
                            e.printStackTrace()
                        }
                        stopLoading()
                    } else {
                    }

                } catch (e: Exception) {
                    Log.d("ambil_lokasi", "Error : ${e.message}")
                }

            }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        if (isLocationPermissionGranted()) {

        }
        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
        } else {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("TAG", "Izin lokasi telah diberikan")
//                Toast.makeText(requireContext(), "Izin lokasi telah diberikan", Toast.LENGTH_SHORT)
//                    .show()
            } else {
                Log.d("TAG", "Izin lokasi ditolak")
//                Toast.makeText(requireContext(), "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun timeRun() {

        runnable = object : Runnable {
            override fun run() {
                val timestamp = System.currentTimeMillis()

                // Create a SimpleDateFormat object with the system timezone
                val sdfHari = SimpleDateFormat("dd", Locale.getDefault())
                val sdfBulan = SimpleDateFormat("MM", Locale.getDefault())
                val sdfTahun = SimpleDateFormat("yyyy", Locale.getDefault())

                val sdfJam = SimpleDateFormat("HH", Locale.getDefault())
                val sdfMenit = SimpleDateFormat("mm", Locale.getDefault())
                val sdfDetik = SimpleDateFormat("ss", Locale.getDefault())

                sdfHari.timeZone = TimeZone.getDefault()
                sdfBulan.timeZone = TimeZone.getDefault()
                sdfTahun.timeZone = TimeZone.getDefault()
                sdfJam.timeZone = TimeZone.getDefault()
                sdfMenit.timeZone = TimeZone.getDefault()
                sdfDetik.timeZone = TimeZone.getDefault()

                // Format the timestamp with the SimpleDateFormat object
                val formattedDateJam = sdfJam.format(Date(timestamp))
                val formattedDateMenit = sdfMenit.format(Date(timestamp))
                val formattedDateDetik = sdfDetik.format(Date(timestamp))
                binding.tvTime.text =
                    "$formattedDateJam : $formattedDateMenit : $formattedDateDetik"


//                binding.textView3.text = formattedDate
//                textViewa.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                binding.tvHari.text = sdfHari.format(Date(timestamp))
                binding.tvBulan.text = sdfBulan.format(Date(timestamp))
                binding.tvTahun.text = sdfTahun.format(Date(timestamp))
                handler.postDelayed(this, 1000)
            }
        }
    }

    fun enableAbsen() {
        binding.imgFinger.background = ContextCompat.getDrawable(requireContext(), R.color._danger)
        binding.btnAbsen.isClickable = true
    }

    private fun disableAbsen() {
        binding.imgFinger.background = ContextCompat.getDrawable(requireContext(), R.color._grey)
        binding.btnAbsen.isClickable = false
    }

    private fun runLoading() {
        binding.currentLocation.text = "-"
        binding.searchLocationLoading.visibility = View.VISIBLE
        binding.searchLocationLoading.playAnimation()
    }

    private fun stopLoading() {

        handler.postDelayed({
            binding.searchLocationLoading.visibility = View.GONE
            binding.searchLocationLoading.cancelAnimation()
        }, 2000)
    }

//    private fun runLottie() {
//
//    }
//
//    private fun stopLottie() {
//        loadingDialog?.let {
//            if (it.isShowing) {
//                it.dismissWithAnimation()
//            }
//        }
//
//    }

}
