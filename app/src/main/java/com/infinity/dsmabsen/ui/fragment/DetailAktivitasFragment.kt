package com.infinity.dsmabsen.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import org.osmdroid.config.Configuration
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentDetailAktivitasBinding
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class DetailAktivitasFragment :
    BaseFragment<FragmentDetailAktivitasBinding>(FragmentDetailAktivitasBinding::inflate) {
    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController

    //    private val args:DetailAktivitasFragmentArgs by navArgs()
    private val args: DetailAktivitasFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupToolbar("Detail Aktivitas")
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
//                R.id.scan -> {
//                    findNavController().navigate(R.id.action_visitFragment2_to_scanFragment)
//                    true
//                }

                else -> false
            }
        }
        setHasOptionsMenu(true)

        val data = args.detailAktivitas

        val fotoUser = data.foto
        val lokasi = data.koordinat

        Log.d("detail", data.foto)
        if (fotoUser.isNotEmpty()) {
            Glide.with(requireContext())
                .load(data.foto)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.image_not_found)
                .into(binding.ivAktivitas)
        } else {
            Glide.with(requireContext())
                .load(fotoUser)
                .placeholder(R.drawable.image_not_found)
                .into(binding.ivAktivitas)
        }

        if (lokasi.isNotEmpty()) {
            setLocation(lokasi)
        }



        binding.tvNama.text = data.nama
        binding.tvTanggalAktivitas.text = data.created_at


    }

    private fun setLocation(lokasi: String) {
        val context: Context = requireContext()
        Configuration.getInstance().load(
            context, context.getSharedPreferences("OSMMap", 0)
        )

        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.setBuiltInZoomControls(true)
        binding.mapView.setMultiTouchControls(true)

        // Set default zoom level and map center
        mapController = binding.mapView.controller as MapController
        mapController.setZoom(18.0)
        val defaultPoint = GeoPoint(-7.7956, 110.3695) // Universitas Gadjah Mada
        mapController.setCenter(defaultPoint)

        // Get latitude and longitude from Aktivitas object

        val latLongArray = lokasi.split("[,\\s]+".toRegex()) // menggunakan regex

        // Set marker and InfoWindow for latitude and longitude
        val latitude = latLongArray[0].toDouble()
        val longitude = latLongArray[1].toDouble()
        val latLng = GeoPoint(latitude, longitude)
        val marker = Marker(binding.mapView)
        marker.position = latLng
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Koordinat"
        binding.mapView.overlays.add(marker)
        binding.mapView.controller.setCenter(latLng)
        binding.mapView.controller.animateTo(latLng)
        binding.mapView.invalidate()

        // Set text for activity name, date, and description
//        binding.namaAktivitasTextView.text = aktivitas.nama
//        binding.tanggalAktivitasTextView.text = aktivitas.tanggal
//        binding.keteranganAktivitasTextView.text = aktivitas.keterangan
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)
        val menuScan = menu.findItem(R.id.scan)

        menuScan.isVisible = false
        menuLogout.isVisible = false
        menuSave?.isVisible = false
        menuPlus?.isVisible = false
    }
}