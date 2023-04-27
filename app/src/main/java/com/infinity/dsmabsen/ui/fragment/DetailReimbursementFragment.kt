package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentDetailReimbursementBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailReimbursementFragment :
    BaseFragment<FragmentDetailReimbursementBinding>(FragmentDetailReimbursementBinding::inflate) {

    private val args: DetailReimbursementFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = args.detailReimbursement
        setupToolbar("Detail Reimbursement")
        binding.apply {
            with(data) {

                tvNominalDetailReimbursement.text = "Rp "+ nilai
                tvStatus.text = status
                tvKeterangan.text = keterangan
                tvTangalDetailPenggajian.text = created_at
            }
        }
    }
}