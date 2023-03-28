package com.example.dsmabsen.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.SpinnerVisitAdapter
import com.example.dsmabsen.databinding.FragmentPenganjuanVisitBinding
import com.example.dsmabsen.helper.AlertDialogHelper
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.VisitViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class PenganjuanVisitFragment :
    BaseFragment<FragmentPenganjuanVisitBinding>(FragmentPenganjuanVisitBinding::inflate) {
    private val CAMERA_PERMISSION_CODE = 100
    private val GALLERY_PERMISSION_CODE = 101

    // Konstanta untuk kode permintaan gambar
    private val CAMERA_REQUEST_CODE = 200
    private val GALLERY_REQUEST_CODE = 201
    val viewModel: VisitViewModel by viewModels()
    val savedUser = Paper.book().read<DataX>("user")
    private var kode_visit: String? = null
    private var kordinat: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            bottomNavigationView.visibility = View.GONE

            uploadFile.setOnClickListener {
                pickImage()
            }
            viewModel.spinnerVisitRequest()
            viewModel.spinnerVisitLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val response = it.data!!
                        val status = response.status
                        val data = response.data
                        if (status) {
//                            val list: ArrayList<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX> = ArrayList()
                            var list = data
                            val defaultItem = DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX(
                                "", 0, 0, "", "", "Pilih lokasi visit", "", "", ""
                            )
                            list = listOf(defaultItem) + list
                            Log.d("apinya", data.toString())

                            val adapter = SpinnerVisitAdapter(requireContext(), list)
                            spinnerVisit.adapter = adapter
                            spinnerVisit.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        if (position != 0) {
                                            val clickedItem: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX =
                                                parent?.getItemAtPosition(position) as DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                                            val currentLocation = clickedItem.kordinat
                                            kode_visit = clickedItem.kode_visit
                                            kordinat = clickedItem.kordinat
                                            mapView.overlays.clear()
                                            getCurrentLocation(currentLocation)

                                            toolbar.toolbar.menu.findItem(R.id.save)?.isVisible =
                                                true
                                        } else {
                                            toolbar.toolbar.menu.findItem(R.id.save)?.isVisible =
                                                false
                                        }
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                        Log.d("select", "onNothingSelected: ")
                                    }
                                }
                            loadingInclude.loading.visibility = View.GONE
                            scrollView2.visibility = View.VISIBLE
                        }
                    }

                    is NetworkResult.Loading -> {
                        loadingInclude.loading.visibility = View.VISIBLE
                        scrollView2.visibility = View.GONE
                    }

                    is NetworkResult.Error -> {
                        handleApiError(it.message)
                    }
                }
            }
        }

        setHasOptionsMenu(true)
        setupToolbar("Visit")
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save -> {
                    val nipRequestBody = MultipartBody.Part.createFormData("nip", savedUser!!.nip)
                    val kodeCutiRequestBody =
                        MultipartBody.Part.createFormData("kode_visit", kode_visit!!)
                    val timeZoneRequestBody = MultipartBody.Part.createFormData("timezone", "")
                    val kordinatRequestBody =
                        MultipartBody.Part.createFormData("kordinat", kordinat!!)
                    val bitmap: Bitmap = (binding.uploadFile.drawable as BitmapDrawable).bitmap
                    val photo = uriToMultipartBody(bitmap)
                    viewModel.sendDataVisitRequest(
                        nipRequestBody.body,
                        kodeCutiRequestBody.body,
                        timeZoneRequestBody.body,
                        kordinatRequestBody.body,
                        photo
                    )
                    viewModel.sendDataVisitLiveData.observe(viewLifecycleOwner) {
                        when (it) {
                            is NetworkResult.Success -> {
                                binding.loadingInclude.loading.visibility = View.GONE
                                binding.scrollView2.visibility = View.VISIBLE
                                val response = it.data!!
                                val status = response.status
                                val message = response.data.messages
                                val alertDialogHelper = AlertDialogHelper(requireContext())
                                alertDialogHelper.showAlertDialog("Judul", message)
                            }

                            is NetworkResult.Loading -> {
                                binding.loadingInclude.loading.visibility = View.VISIBLE
                                binding.scrollView2.visibility = View.GONE
                            }

                            is NetworkResult.Error -> {
                                binding.loadingInclude.loading.visibility = View.GONE
                                handleApiError(it.message)
                            }
                        }
                    }
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun pickImage() {
        val pictureDialog = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Take Photo With Camera", "Select Photo From Gallery")
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        takePictureFromCamera()
                    } else {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.CAMERA),
                            CAMERA_REQUEST_CODE
                        )
                    }
                }
                1 -> {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        choosePictureFromGallery()
                    } else {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            GALLERY_PERMISSION_CODE
                        )
                    }
                }
            }
        }
        pictureDialog.show()
    }

    private fun takePictureFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun choosePictureFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.uploadFile.setImageBitmap(imageBitmap)
        }

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri = data.data!!
            binding.uploadFile.setImageURI(imageUri)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePictureFromCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (requestCode == GALLERY_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePictureFromGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Gallery permission denied",
                    Toast.LENGTH_SHORT
                ).show()
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

    private fun FragmentPenganjuanVisitBinding.getCurrentLocation(currentLocation: String): Boolean {
        Configuration.getInstance().load(
            requireContext(),
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
        )

        // Inisialisasi MapView dan set tipe sumber tiles
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)

        val latLngParts = currentLocation.split(",")
        val latitude = latLngParts[0].toDouble()
        val longitude = latLngParts[1].toDouble()
        val initialLocation = GeoPoint(latitude, longitude)
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(initialLocation)

        val marker = Marker(mapView)
        marker.position = initialLocation
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        return mapView.overlays.add(marker)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)

        menuLogout.isVisible = false
        menuSave?.isVisible = false
        menuPlus?.isVisible = false
    }

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            toolbar.toolbar.visibility = View.VISIBLE
            scrollView2.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            scrollView2.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
//        val navHostFragment =
//            childFragmentManager.findFragmentById(R.id.main_nav) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        val isBottomNavVisible =
//            navController.currentDestination?.id == R.id.homeFragment || navController.currentDestination?.id == R.id.profileFragment
//
//        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
//            if (isBottomNavVisible) View.VISIBLE else View.GONE
    }
}

