package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentDetailLemburBinding

class DetailLemburFragment :
    BaseFragment<FragmentDetailLemburBinding>(FragmentDetailLemburBinding::inflate) {

    private val args:DetailLemburFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar("Detail Lembur")
        val data = args.dataLembur
        with(binding){
            tvDurasi.text = data.jam_mulai +"-"+ data.jam_selesai
            tvPadaTgl.text = data.tanggal
            tvKeterangan.text = data.keterangan
            tvStatus.text = data.status
            tvTangalDetailPenggajian.text = data.created_at

        }

    }

}