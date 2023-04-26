package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentDetailShiftBinding

class DetailShiftFragment :
    BaseFragment<FragmentDetailShiftBinding>(FragmentDetailShiftBinding::inflate) {

    private val args:DetailShiftFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val data = args.detailShift
            setupToolbar("Detail Shift")
            tvDurasi.text = data.shift
            tvKeterangan.text = data.keterangan
            tvStatus.text = data.status_api
            tvTangalDetailPerizinan.text = data.created_at
        }
    }

}