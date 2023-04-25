package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentDetailPerizinanBinding

class DetailPerizinanFragment :
    BaseFragment<FragmentDetailPerizinanBinding>(FragmentDetailPerizinanBinding::inflate) {

    private val args: DetailPerizinanFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val data = args.detailPerizinan
            setupToolbar("Detail Perizinan")
            tvDurasi.text = data.tanggal_mulai + "-" + data.tanggal_selesai
            tvKeterangan.text = data.keterangan
            tvPadaTgl.text = data.created_at
            tvStatus.text = data.status
        }
    }

}