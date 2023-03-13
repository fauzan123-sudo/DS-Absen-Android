package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentDetailGajiBinding

class DetailGajiFragment :BaseFragment<FragmentDetailGajiBinding>(FragmentDetailGajiBinding::inflate){

    private val args by navArgs<DetailGajiFragmentArgs>()

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

        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu

        val item = menu.findItem(R.id.save)
        item.setActionView(R.layout.item_menu_toolbar)

        val actionView = item.actionView
        val btnSimpan = actionView?.findViewById<TextView>(R.id.textSimpan)
        btnSimpan?.setOnClickListener {
        }
    }
}