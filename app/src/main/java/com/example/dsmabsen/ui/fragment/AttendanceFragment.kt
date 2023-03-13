package com.example.dsmabsen.ui.fragment

import android.Manifest.permission.*
import android.app.Activity
import android.util.Base64
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build

import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentAttendanceBinding
import com.example.dsmabsen.helper.*
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.AttendanceViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.time.format.TextStyle

@AndroidEntryPoint
class AttendanceFragment :
    BaseFragment<FragmentAttendanceBinding>(FragmentAttendanceBinding::inflate) {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private val LOCATION_PERMISSION_CODE = 123 // kode permintaan izin
    private val viewModel: AttendanceViewModel by viewModels()
    private val handler = Handler()
    private lateinit var runnable: Runnable

    @Inject
    lateinit var tokenManager: TokenManager
    private val savedUser = Paper.book().read<DataX>("user")


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        hideToolbar()
        timeRun()

        binding.apply {

            val permission =
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, ACCESS_BACKGROUND_LOCATION)
//            permissionMultiRequest.launch(permission)
            requestLocationPermission()
            locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            Toast.makeText(context, "$locationManager", Toast.LENGTH_SHORT).show()

            cekGPS()

            btnAbsen.setOnClickListener {
                camera()
            }
            namaUser.text = savedUser!!.name
            jabatan.text = savedUser!!.nama_jabatan
            Glide.with(requireContext())
                .load(Constans.IMAGE_URL + savedUser!!.image)
                .into(imageUser)


            imageUser.setOnClickListener {
                findNavController().navigate(R.id.action_attendanceFragment_to_profileFragment)
            }
            btnRefresh.setOnClickListener {
                cekGPS()
            }
        }


    }

    override fun onStart() {
        super.onStart()

        getLocation()
    }

    private val permissionMultiRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            var areAllGranted = true
            for (isGranted in result.values) {
//                Toast.makeText(requireContext(), "Granted is $isGranted", Toast.LENGTH_SHORT).show()
                areAllGranted = areAllGranted && isGranted
            }

            if (areAllGranted) {
//                Toast.makeText(
//                    requireContext(),
//                    "Permintaan Izin di aktifkan",
//                    Toast.LENGTH_SHORT
//                ).show()
                val locationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        // Do something with the new location
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                    override fun onProviderEnabled(provider: String) {
                        if (provider == LocationManager.GPS_PROVIDER) {
//                            Toast.makeText(
//                                requireContext(),
//                                "Provider $provider enabled",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    }

                    override fun onProviderDisabled(provider: String) {
//                        Toast.makeText(
//                            requireContext(),
//                            "Provider $provider disabled",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }
                }
                requestLocationPermission()
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    locationListener
                )

                getLocation()
            } else {
//                Toast.makeText(
//                    requireContext(),
//                    "Permintaan Izin di nonaktifkan",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }


    private fun getLocationUser() {
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                with(binding) {
                    lattitudeUser.text = it.latitude.toString()
                    longitudeUser.text = it.longitude.toString()

                }
                getAddressAboveSDK29()
            }
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
        getLocationUser()
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Mendapatkan latitude dan longitude
            val latitude = location.latitude
            val longitude = location.longitude

            binding.lattitudeUser.text = latitude.toString()
            binding.longitudeUser.text = longitude.toString()

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {
            getLocationUser()
            enableAbsen()
        }

        override fun onProviderDisabled(provider: String) {
            cekGPS()
        }
    }

    override fun onStop() {
        super.onStop()
        locationManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private val putPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                val selectedImageUris = it?.data?.extras?.get("data") as Bitmap
                val uri = it?.data?.data
                absen(selectedImageUris!!)

//                binding.photo.setImageBitmap(selectedImageUris)            /* imgPhoto.load(bitmap) */
//                binding.photo.isVisible = true
            } else if (it == null) {
                Toast.makeText(requireContext(), "Gambar tida dapat di set", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }

        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun absen(uri: Bitmap) {

        val photo = uriToMultipartBody(uri)


        Log.d("gambar", photo.toString())

        val timeNow = getTime()

        val nipRequestBody = MultipartBody.Part.createFormData("nip", savedUser!!.nip)
        val dateRequestBody = MultipartBody.Part.createFormData("date", timeNow)
        val timezoneRequestBody = MultipartBody.Part.createFormData("timezone", "")
        val kordinatRequestBody = MultipartBody.Part.createFormData("kordinat", "123, 456")
        val kodeShiftRequestBody = MultipartBody.Part.createFormData("kode_shift", "12")
        val kodeTingkatRequestBody = MultipartBody.Part.createFormData("kode_tingkat", "0")

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
                    binding.loadingInclude.loading.isVisible = false

                    val response = it.data!!
                    val statusServer = response.status
                    val statusPresensi = response.data.status
                    val message = response.data.messages
                    if (statusPresensi == "Error"){
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
                    }else{
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

//                    Toast.makeText(requireContext(),message , Toast.LENGTH_SHORT).show()



                }
                is NetworkResult.Loading -> {
                    binding.loadingInclude.loading.isVisible = true
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Error -> {
                    handleApiError(it.message)
                }
            }
        }


    }

    private fun getTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    private fun uriToMultipartBody(bitmap: Bitmap): MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val requestFile = byteArray.toRequestBody("image/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", "image.png", requestFile)
    }

    private val singlePermissionLaunch =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Log.d(Constans.TAG, "permission is Granted: $isGranted")
            if (isGranted) {
                Log.d("TAG", "permission granted")
//                Toast.makeText(requireContext(), "Permission camera is granted", Toast.LENGTH_SHORT)
//                    .show()

            } else {
                Log.d(Constans.TAG, "Permission camera  : Permission Denied ")
//                Toast.makeText(requireContext(), "Permission camera Denied ", Toast.LENGTH_SHORT)
//                    .show()
            }
        }

    private fun getAddressAboveSDK29() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Mendapatkan alamat pengguna berdasarkan lokasi yang diperoleh
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                geocoder.getFromLocation(
                    location?.latitude ?: 0.0,
                    location?.longitude ?: 0.0,
                    1
                ).also { addresses ->
                    // Mengambil alamat dari objek Address dan menampilkannya pada TextView
                    val address: String? = addresses!![0].getAddressLine(0)
//                    Toast.makeText(requireContext(), "alamat golekai $address", Toast.LENGTH_SHORT).show()

                    val city: String? = addresses[0].locality
                    val state: String? = addresses[0].adminArea
                    val country: String? = addresses[0].countryName
                    val postalCode: String? = addresses[0].postalCode
                    val knownName: String? = addresses[0].featureName

                    binding.currentLocation.text =
                        "$address"
//                    Toast.makeText(context, "$address", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getAddressBelowSDK29() {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Mendapatkan alamat pengguna berdasarkan lokasi yang diperoleh
                val geocoder = Geocoder(requireContext(), Locale.getDefault())

                val addresses = geocoder.getFromLocation(
                    location?.latitude ?: 0.0,
                    location?.longitude ?: 0.0,
                    1
                )
                // Mengambil alamat dari objek Address dan menampilkannya pada TextView
                val address: String? = addresses!![0].getAddressLine(0)
                val city: String? = addresses[0].locality
                val state: String? = addresses[0].adminArea
                val country: String? = addresses[0].countryName
                val postalCode: String? = addresses[0].postalCode
                val knownName: String? = addresses[0].featureName

                binding.currentLocation.text =
                    "$address"
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

//            Toast.makeText(
//                requireContext(),
//                "Anda telah memberikan izin lokasi",
//                Toast.LENGTH_SHORT
//            )
//                .show()
        }
        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
//            Toast.makeText(
//                requireContext(),
//                "Izin lokasi dibutuhkan untuk melakukan absensi",
//                Toast.LENGTH_SHORT
//            ).show()
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
//                Log.d("run jam", "Formatted date with system timezone: $formattedDate")

//                val currentTime = System.currentTimeMillis()
//                val seconds = (currentTime / 1000) % 60
//                val minutes = (currentTime / (1000 * 60)) % 60
//                val hours = (currentTime / (1000 * 60 * 60)) % 24
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

    fun cekGPS() {
        // Cek apakah GPS sedang aktif
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnabled) {

            getLocation()
        } else {
            // GPS tidak aktif, tampilkan dialog untuk meminta pengguna mengaktifkannya
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("GPS tidak aktif, aktifkan GPS?")
                .setCancelable(false)
                .setPositiveButton("Ya") { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    enableAbsen()
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    disableAbsen()
                    dialog.cancel()
                }
            val alert = builder.create()
            alert.show()
        }
    }


    fun mematikanGPS() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)?.let { isGPSOn ->
            if (isGPSOn) {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
    }

    fun menyalakanGPS() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)?.let { isGPSOn ->
            if (!isGPSOn) {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
    }

    fun enableAbsen() {
        binding.imgFinger.background = resources.getDrawable(R.color._danger)
        binding.btnAbsen.isClickable = true
    }

    private fun disableAbsen() {
        binding.imgFinger.background = resources.getDrawable(R.color._grey)
        binding.btnAbsen.isClickable = false
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 0)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}
