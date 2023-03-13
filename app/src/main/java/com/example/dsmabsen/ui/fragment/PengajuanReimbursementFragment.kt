package com.example.dsmabsen.ui.fragment

import DataSpinnerAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.SpinnerReimbursementAdapter
import com.example.dsmabsen.databinding.FragmentPengajuanReimbursementBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXX
import com.example.dsmabsen.model.SpinnerReimbursement
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.ReimbursementViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import java.math.BigDecimal
import java.text.DecimalFormat

@AndroidEntryPoint
class PengajuanReimbursementFragment :
    BaseFragment<FragmentPengajuanReimbursementBinding>(FragmentPengajuanReimbursementBinding::inflate) {
    private lateinit var adapter: DataSpinnerAdapter

    private var selectedJenisReimbursement: DataXXXXXXXXXXXXXXXXXXXXXXXXXXX? = null
    var kodeReimbursement2: String? = null
    val viewModel: ReimbursementViewModel by viewModels()
    val savedUser = Paper.book().read<DataX>("user")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setHasOptionsMenu(true)
        setupToolbar("Ajukan Reimbursement")
        view.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.save -> {
                        binding.apply {
                            val nominal = edtNominal.text.toString()
                            val keterangan = edtKeterangan.text.toString()

                            if (nominal.isEmpty()) {
                                edtNominal.error = "Harap isi bidang ini!!"
                                edtNominal.requestFocus()
                            } else if (keterangan.isEmpty()) {
                                edtKeterangan.error = "harap isi keterangan"
                                edtKeterangan.requestFocus()
                            } else {
                                saveReimbursement(savedUser)
                            }
                        }
                        true
                    }

                    else -> false
                }
            }
        }


        binding.apply {


            viewModel.requestSpinnerReimbursement()
            viewModel.getSpinnerReimbursementLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val response = it.data!!
                        val data = response.data

                        adapter = DataSpinnerAdapter(requireContext(), data)
                        spinnerJenisReimbursement.adapter = adapter
                        spinnerJenisReimbursement.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                                ) {
                                    val clickedItem : DataXXXXXXXXXXXXXXXXXXXXXXXXXXX=
                                        parent?.getItemAtPosition(position) as DataXXXXXXXXXXXXXXXXXXXXXXXXXXX
                                    val kodeReimbursement = clickedItem.kode_reimbursement
                                    kodeReimbursement2 = kodeReimbursement

//                                    kodeReimbursement2 =
//                                        selectedJenisReimbursement!!.kode_reimbursement
                                    Log.d(
                                        "TAG",
                                        "onItemSelected: ${selectedJenisReimbursement!!.kode_reimbursement} "
                                    )
//                                    Toast.makeText(
//                                        context,
//                                        selectedJenisReimbursement!!.kode_reimbursement,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    selectedJenisReimbursement = null
                                }

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)

        menuSave?.isVisible = true // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu

        val item = menu.findItem(R.id.save)
        item.setActionView(R.layout.item_menu_toolbar)

        val actionView = item.actionView
        val btnSimpan = actionView?.findViewById<TextView>(R.id.textSimpan)
        btnSimpan?.setOnClickListener {
            if (binding.edtNominal.text.toString()
                    .isEmpty() && binding.edtKeterangan.text.toString().isEmpty()
            ) {
                saveReimbursement(savedUser)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Harap isi nominal dan keterangan dshulu!! ",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    private fun saveReimbursement(savedUser: DataX?) {
        viewModel.requestPengajuanReimbursement(
            savedUser!!.nip,
            kodeReimbursement2!!,
            binding.edtNominal.text.toString(),
            "",
            binding.edtKeterangan.text.toString()
        )
        viewModel.getPengajuanLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val response = it.data!!
                    val message = response.data.messages
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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