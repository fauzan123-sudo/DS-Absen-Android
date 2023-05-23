package com.infinity.dsmabsen.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.adapter.SpinnerVisitAdapter
import com.infinity.dsmabsen.databinding.FragmentPenganjuanVisitBinding
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.activity.MainActivity
import com.infinity.dsmabsen.ui.viewModel.VisitViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class PenganjuanVisitFragment :
    BaseFragment<FragmentPenganjuanVisitBinding>(FragmentPenganjuanVisitBinding::inflate),
    LocationListener {
    private lateinit var mapController: IMapController
    private lateinit var myLocationNewOverlay: MyLocationNewOverlay
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private val data = arguments?.getString("data")
    private lateinit var mapView: MapView
    private var provider: String? = null
    private lateinit var adapter: SpinnerVisitAdapter

    private val argss by navArgs<PenganjuanVisitFragmentArgs>()
    val viewModel: VisitViewModel by viewModels()
    val savedUser = Paper.book().read<DataX>("user")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSpinnerVisit()

        Configuration.getInstance().userAgentValue = "DSM-Absen"
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = this

        with(binding) {
            addImage.setOnClickListener {
                camera()
            }
            getFirstLocation()
            bottomNavigationView.visibility = View.GONE
            Log.d("args", argss.barcodes)
        }

        setHasOptionsMenu(true)
        setupToolbar("Pengajuan Visit")
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save -> {

//                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
//                    Log.d("tombol", "onViewCreated: ")
                    sendData()
//                    getLocations()
                    true
                }

                else -> false
            }
        }
    }

    private fun getSpinnerVisit() {
        viewModel.spinnerVisitRequest()
        viewModel.spinnerVisitLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val responses = it.data!!
                    with(binding) {
                        loadingInclude.loading.visibility = View.GONE
                        scrollView2.visibility = View.VISIBLE
                        adapter = SpinnerVisitAdapter(requireContext(), responses.data)
                        binding.spinnerKodeVisit.adapter = adapter
                    }
                }

                is NetworkResult.Loading -> {
                    with(binding) {
                        loadingInclude.loading.visibility = View.VISIBLE
                        scrollView2.visibility = View.GONE

                    }
                }

                is NetworkResult.Error -> {
                    with(binding) {
                        loadingInclude.loading.visibility = View.GONE
                        scrollView2.visibility = View.VISIBLE

                    }
                }
            }
        }
    }

    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        putPhoto.launch(intent)
    }

    private val putPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val bitmap = it?.data?.extras?.get("data") as Bitmap
                binding.imgUpload.setImageBitmap(bitmap)            /* imgPhoto.load(bitmap) */
            } else if (it == null) {
                binding.imgUpload.setImageResource(R.drawable.ic_launcher_background)
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }

        }

    private fun sendData() {
        AlertDialog.Builder(requireContext())
            .setMessage("Anda yakin ingin mengajukan visit")
            .setPositiveButton("Ya") { _, _ ->
                sendVisit()
            }
            .setNegativeButton("Kembali") { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()
    }

    private fun sendVisit() {

        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                location?.let {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    val kordinat: String? = "$latitude, $longitude"
//                    binding.tvLokasi.text = "$kordinat"
                    Log.d("TAG", "sendVisit: $kordinat")
                    val selectedItem =
                        binding.spinnerKodeVisit.selectedItem as DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX?
                    if (selectedItem != null) {
                        Log.d("MyActivity", "Current Visit Code: ${selectedItem.kode_visit}")
                    }
                    val nipRequestBody = MultipartBody.Part.createFormData("nip", savedUser!!.nip)
                    val kodeVisitRequestBody =
                        MultipartBody.Part.createFormData("kode_visit", selectedItem!!.kode_visit)
                    val koordinatVisitRequestBody =
                        MultipartBody.Part.createFormData("kordinat", kordinat!!)
                    val bitmap: Bitmap = (binding.imgUpload.drawable as BitmapDrawable).bitmap
                    val photo = uriToMultipartBody(bitmap)
                    viewModel.sendDataVisitRequest(
                        nipRequestBody.body,
                        kodeVisitRequestBody.body,
                        koordinatVisitRequestBody.body,
                        photo
                    )

                    viewModel.sendDataVisitLiveData.observe(viewLifecycleOwner) {
                        when (it) {
                            is NetworkResult.Success -> {
                                binding.apply {
                                    loadingInclude.loading.visibility = View.GONE
                                    scrollView2.visibility = View.VISIBLE
                                    val response = it.data!!
                                    val status = response.data.status
                                    val message = response.data.messages
                                    AlertDialog.Builder(requireContext())
                                        .setMessage(message)
                                        //    .setIcon(R.drawable.ic_warning)
                                        .setPositiveButton("Ok") { _, _ ->
                                            val action =
                                                PenganjuanVisitFragmentDirections.actionPenganjuanVisitFragmentToVisitFragment2()
                                            if (action.actionId != R.id.action_penganjuanVisitFragment_to_visitFragment2) {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Destinasi tidak ditemukan",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                findNavController().navigate(action)
                                            }

//                                            val moveFragment =
//                                                findNavController().navigate(R.id.action_penganjuanVisitFragment_to_visitFragment2)
//                                            findNavController().navigate(R.id.action_penganjuanVisitFragment_to_visitFragment2)
                                        }
//                                        .setNegativeButton("Kembali") { dialog, _ ->
//                                            dialog.dismiss()
//                                        }
                                        .create().show()
                                }
                            }

                            is NetworkResult.Loading -> {
                                binding.apply {
                                    loadingInclude.loading.visibility = View.VISIBLE
                                    scrollView2.visibility = View.GONE
                                }


                            }

                            is NetworkResult.Error -> {
                                binding.apply {
                                    handleApiError(it.message)
                                    loadingInclude.loading.visibility = View.GONE
                                    scrollView2.visibility = View.VISIBLE
                                }
                            }
                        }
                    }

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

    private fun getFirstLocation() {
        val map = binding.mapView
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        map.setMultiTouchControls(true)
        map.controller.setZoom(15.0)
        mapController = map.controller
        myLocationNewOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), map)
        myLocationNewOverlay.enableMyLocation()
        map.overlays.add(myLocationNewOverlay)
        if (myLocationNewOverlay.myLocation != null) {
            val latitude = myLocationNewOverlay.myLocation.latitude
            val longitude = myLocationNewOverlay.myLocation.longitude
            Log.d("latitude", "$latitude")
            Log.d("longitude", "$longitude")
//                progressBar.visibility = View.INVISIBLE
//                Toast.makeText(requireContext(), "lokasi ditemukan", Toast.LENGTH_SHORT).show()
        } else {
//                Toast.makeText(requireContext(), "lokasi belum ditemukan", Toast.LENGTH_LONG).show()
            Log.d("lokasi", "lokasi tidak ditemukan")
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            updateLocation()
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            Toast.makeText(
                requireContext(),
                "Izin akses lokasi tidak diberikan",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateLocation() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocation()
                } else {
                    // Izin ditolak, menampilkan pesan peringatan
                    AlertDialog.Builder(requireContext())
                        .setTitle("Izin Diperlukan")
                        .setMessage("Aplikasi ini memerlukan izin untuk mengakses lokasi Anda.")
                        .setPositiveButton("Izinkan") { _, _ ->
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                1
                            )
                        }
                        .setNegativeButton("Tolak") { _, _ -> }
                        .create()
                        .show()
                }
            }
        }
    }


    private fun FragmentPenganjuanVisitBinding.getCurrentLocation() {
        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )

        with(binding) {
            // Inisialisasi MapView dan set tipe sumber tiles
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            mapView.setMultiTouchControls(true)
            mapView.controller.setZoom(15.0)
            mapController = mapView.controller
            myLocationNewOverlay =
                MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView)
            myLocationNewOverlay.enableMyLocation()
            mapView.overlays.add(myLocationNewOverlay)
            if (myLocationNewOverlay.myLocation != null) {
                val latitude = myLocationNewOverlay.myLocation.latitude
                val longitude = myLocationNewOverlay.myLocation.longitude
                Log.d("latitude", "$latitude")
                Log.d("longitude", "$longitude")
//                progressBar.visibility = View.INVISIBLE
//                Toast.makeText(requireContext(), "lokasi ditemukan", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(requireContext(), "lokasi belum ditemukan", Toast.LENGTH_LONG).show()
                Log.d("lokasi", "lokasi tidak ditemukan")
            }
        }
    }

    fun cekGPS() {
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsEnabled) {
            // GPS tidak aktif, tampilkan dialog untuk meminta pengguna mengaktifkannya
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("GPS tidak aktif, aktifkan GPS?")
                .setCancelable(false)
                .setPositiveButton("Ya") { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.cancel()
                }
            val alert = builder.create()
            alert.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigation()
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)
        val menuScan = menu.findItem(R.id.scan)

        menuScan.isVisible = false
        menuLogout.isVisible = false
        menuSave?.isVisible = true
        menuPlus?.isVisible = false
    }

//    override fun onConnectionAvailable() {
//        super.onConnectionAvailable()
//        binding.apply {
//            toolbar.toolbar.visibility = View.VISIBLE
//            scrollView2.visibility = View.VISIBLE
//            noInternetConnection.ivNoConnection.visibility = View.GONE
//        }
//    }
//
//    override fun onConnectionLost() {
//        super.onConnectionLost()
//        binding.apply {
//            toolbar.toolbar.visibility = View.GONE
//            scrollView2.visibility = View.GONE
//            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
//        }
//    }

    override fun onResume() {
        super.onResume()
        provider = locationManager.getBestProvider(Criteria(), false)
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
    }

    override fun onStart() {
        Log.d("TAG", "onStart: ")
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        super.onStart()
    }

    override fun onPause() {
        Log.d("TAG", "onPause: ")
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        super.onPause()
    }

    override fun onStop() {
        Log.d("TAG", "onStop: ")
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        super.onStop()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("TAG", "onDetach: ")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TAG", "onAttach: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        hideBottomNavigation()
        Log.d("TAG", "onCreate: ")
    }

    override fun onLocationChanged(location: Location) {
//        Toast.makeText(requireContext(), "jalan", Toast.LENGTH_SHORT).show()
//        progressBar.visibility = View.GONE
        mapController.setCenter(GeoPoint(location.latitude, location.longitude))
    }

}

