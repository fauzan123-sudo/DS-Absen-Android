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
import android.provider.MediaStore
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
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AttendanceFragment :
    BaseFragment<FragmentAttendanceBinding>(FragmentAttendanceBinding::inflate) {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private val LOCATION_PERMISSION_CODE = 123 // kode permintaan izin
    private val viewModel: AttendanceViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager
    private val savedUser = Paper.book().read<DataX>("user")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        setupToolbar("Presensi")
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save -> {
                    // Handle add menu item click
//                        saveReimbursement(savedUser)
                    true
                }

                else -> false
            }
        }
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            val permission =
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, ACCESS_BACKGROUND_LOCATION)
//            permissionMultiRequest.launch(permission)
            requestLocationPermission()
            locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            btnAbsen.setOnClickListener {
                camera()
            }

        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)

        menuSave?.isVisible = true // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu

        val item = menu.findItem(R.id.save)
        item.setActionView(R.layout.item_menu_toolbar)

        val actionView = item.actionView
        val btnSimpan = actionView?.findViewById<TextView>(R.id.textSimpan)
        btnSimpan?.setOnClickListener {
            // your code here
//            saveReimbursement(savedUser)

        }

    }
    override fun onStart() {
        super.onStart()

        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        getLocationUser()
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
                val locationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        // Do something with the new location
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                    override fun onProviderEnabled(provider: String) {
                        Toast.makeText(
                            requireContext(),
                            "Provider $provider enabled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onProviderDisabled(provider: String) {
                        Toast.makeText(
                            requireContext(),
                            "Provider $provider disabled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    locationListener
                )
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
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                with(binding) {
                    lattitudeUser.text = it.latitude.toString()
                    longitudeUser.text = it.longitude.toString()
                }
                if (Build.VERSION.SDK_INT >= 29) {
                    getAddressAboveSDK29()
                } else {
                    getAddressBelowSDK29()
                }
            }
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
        val timeNow: String = if (Build.VERSION.SDK_INT >= 26) {
            Helper().getMYDetail()
        }else{
            HelperApiUnder().getTimeDetail()
        }

        val nipRequestBody = MultipartBody.Part.createFormData("nip", savedUser!!.nip)
        val dateRequestBody = MultipartBody.Part.createFormData("date", timeNow)
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
                    binding.loadingInclude.loading.isVisible = false
                    if (it.data!!.data.status == "Success") {
                        Toast.makeText(requireContext(), "success upload file", Toast.LENGTH_LONG)
                            .show()

                        AlertDialog.Builder(requireContext())
                            .setTitle("Peringatan")
                            .setMessage("Anda Harus memberika izin untuk scan")
                            //    .setIcon(R.drawable.ic_warning)
                            .setPositiveButton("Ya") { _, _ ->
                                Toast.makeText(requireContext(), "Ya", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("Kembali") { _, _ ->
                                Toast.makeText(requireContext(), "Kembali", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            .create().show()
                        binding.materialCardView21.isVisible = true
                        binding.notificationUser.isVisible = true
                        binding.informationLayout.isVisible = true
                        binding.notificationUser.text = "Berhasil absen"
                        binding.informationLayout.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color._success
                            )
                        )
                    } else {
                        Toast.makeText(requireContext(), "error upload file", Toast.LENGTH_LONG)
                            .show()

                        binding.materialCardView21.isVisible = true
                        binding.notificationUser.isVisible = true
                        binding.informationLayout.isVisible = true
                        binding.notificationUser.text = "gagal absen"
                        binding.informationLayout.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color._danger
                            )
                        )
                    }
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
                Toast.makeText(requireContext(), "Permission camera is granted", Toast.LENGTH_SHORT)
                    .show()

            } else {
                Log.d(Constans.TAG, "Permission camera  : Permission Denied ")
                Toast.makeText(requireContext(), "Permission camera Denied ", Toast.LENGTH_SHORT)
                    .show()
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
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
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
                    val city: String? = addresses[0].locality
                    val state: String? = addresses[0].adminArea
                    val country: String? = addresses[0].countryName
                    val postalCode: String? = addresses[0].postalCode
                    val knownName: String? = addresses[0].featureName

                    binding.currentLocation.text =
                        "$address"
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
            Toast.makeText(requireContext(), "Anda telah memberikan izin lokasi", Toast.LENGTH_SHORT).show()
        }
        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
            Toast.makeText(requireContext(), "Izin lokasi dibutuhkan untuk melakukan absensi", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Izin lokasi telah diberikan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }



}
