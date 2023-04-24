package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.View
import com.infinity.dsmabsen.databinding.FragmentDetailReimbursementBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailReimbursementFragment :
    BaseFragment<FragmentDetailReimbursementBinding>(FragmentDetailReimbursementBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar("Detail Reimbursement")


    }
}