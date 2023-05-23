package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentDetailGajiBinding

class DetailGajiFragment :BaseFragment<FragmentDetailGajiBinding>(FragmentDetailGajiBinding::inflate){

//    private val args by navArgs<DetailGajiFragmentArgs>()
    private val args: DetailGajiFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)
        setupToolbar("Detail Gaji")
        val data = args.detailGaji

        binding.apply {
            tvNominalDetailSlipgaji.text = data.gaji_pokok
            tvNominalGajiPokok.text = data.gaji_pokok
            tvJabatan.text = data.jabatan
            tvUangMakan.text = ""
            tvTangalDetailPenggajian.text = data.tanggal
            tvIdDetailPenggajian.text = data.kode_payroll
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)

        menuLogout.isVisible = false
        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu

        val item = menu.findItem(R.id.save)
        item.setActionView(R.layout.item_menu_toolbar_simpan)

        val actionView = item.actionView
        val btnSimpan = actionView?.findViewById<TextView>(R.id.textSimpan)
        btnSimpan?.setOnClickListener {
        }
    }

//    override fun onConnectionAvailable() {
//        super.onConnectionAvailable()
//        binding.apply {
//            toolbar.toolbar.visibility = View.VISIBLE
//            card1.visibility = View.VISIBLE
//            noInternetConnection.ivNoConnection.visibility = View.GONE
//        }
//    }
//
//    override fun onConnectionLost() {
//        super.onConnectionLost()
//        binding.apply {
//            toolbar.toolbar.visibility = View.GONE
//            card1.visibility = View.GONE
//            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
//        }
//    }


}