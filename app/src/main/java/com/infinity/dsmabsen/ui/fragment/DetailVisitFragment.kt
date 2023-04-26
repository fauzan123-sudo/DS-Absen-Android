package com.infinity.dsmabsen.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.infinity.dsmabsen.databinding.FragmentDetailVisitBinding
import com.infinity.dsmabsen.model.DataX
import io.paperdb.Paper
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.Marker

class DetailVisitFragment :
    BasesFragment<FragmentDetailVisitBinding>(FragmentDetailVisitBinding::inflate) {

    private val args:DetailVisitFragmentArgs by navArgs()
    val savedUser = Paper.book().read<DataX>("user")
    private lateinit var mapController: IMapController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            (activity as AppCompatActivity).setSupportActionBar(toolbar.toolbarImage)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

            toolbar.toolbarImage.title = "Detail Visit"
            toolbar.toolbarImageView.visibility = View.INVISIBLE

            val data = args.detailVisit
            val koordinat = data.kordinat

            tvTanggal.text = data.tanggal
            tvJam.text = data.jam
//            tvKodeVisit.text = data.kode_visit
            tvLokasiVisit.text = data.visit?:""

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
            val defaultPoint = GeoPoint(-7.7956, 110.3695)
            mapController.setCenter(defaultPoint)

            // Get latitude and longitude from Aktivitas object

            val latLongArray = koordinat!!.split("[,\\s]+".toRegex())

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

        }
    }
}