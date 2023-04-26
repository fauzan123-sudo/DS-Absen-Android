package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.infinity.dsmabsen.databinding.FragmentDetailDataAbsenBinding

class DetailDataAbsenFragment :
    BaseFragment<FragmentDetailDataAbsenBinding>(FragmentDetailDataAbsenBinding::inflate) {

    private val args: DetailDataAbsenFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar("Detail Absen")
        val data = args.dataAbsen

        with(binding){
            tvAbsen.text = data.absen?: "-"
            if (data.status == 1) {
                tvStatus.text = "Check in"
            } else {
                tvStatus.text = "Check out"
            }
            tvPadaTgl.text = data.tanggal
        }
    }

}