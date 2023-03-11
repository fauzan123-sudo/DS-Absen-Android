package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentPengajuanReimbursementBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.ReimbursementViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class PengajuanReimbursementFragment :
    BaseFragment<FragmentPengajuanReimbursementBinding>(FragmentPengajuanReimbursementBinding::inflate) {

    val viewModel: ReimbursementViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val savedUser = Paper.book().read<DataX>("user")
        with(binding) {
            viewModel.requestPengajuanReimbursement(
                savedUser!!.nip,
                "1",
                edtNominal.text.toString(),
                "",
                edtKeterangan.text.toString()
            )
            viewModel.getPengajuanLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val response = it.data!!
                        val status = response.status
                        val statusPengajuan = response.data.status
                        if (status && statusPengajuan) {
                            Toast.makeText(
                                requireContext(),
                                "anda berhasil mengajukan reimbursement",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                response.data.messages,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    is NetworkResult.Loading -> {
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }

                    is NetworkResult.Error -> {
                        handleApiError(it.message)
                    }
                }
            }
        }

    }
}