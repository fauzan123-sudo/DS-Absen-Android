package com.example.dsmabsen.ui.fragment

import DataSpinnerAdapter
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentPengajuanReimbursementBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.ReimbursementViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

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
        binding.apply {


            viewModel.requestSpinnerReimbursement()
            viewModel.getSpinnerReimbursementLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        loadingInclude.loading.visibility = View.GONE
                        val response = it.data!!
                        val data = response.data
                        adapter = DataSpinnerAdapter(requireContext(), data)
                        spinnerJenisReimbursement.adapter = adapter
                        spinnerJenisReimbursement.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                                ) {
                                    val clickedItem: DataXXXXXXXXXXXXXXXXXXXXXXXXXXX =
                                        parent?.getItemAtPosition(position) as DataXXXXXXXXXXXXXXXXXXXXXXXXXXX
                                    val kodeReimbursement = clickedItem.kode_reimbursement
                                    kodeReimbursement2 = kodeReimbursement
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    selectedJenisReimbursement = null
                                }

                            }
                    }

                    is NetworkResult.Loading -> {
                        loadingInclude.loading.visibility = View.VISIBLE

                    }

                    is NetworkResult.Error -> {
                        loadingInclude.loading.visibility = View.GONE
                        handleApiError(it.message)
                    }
                }
            }

        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save -> {
                    if (binding.edtNominal.text.toString()
                            .isNotEmpty() && binding.edtKeterangan.text.toString().isNotEmpty()
                    ) {
                        saveReimbursement(savedUser)
                    }
                    if (binding.edtNominal.text.isEmpty()) {
                        binding.edtNominal.error = "Harap isi bidang ini!!"
                        binding.edtNominal.requestFocus()
                    }
                    if (binding.edtKeterangan.text.isEmpty()) {
                        binding.edtKeterangan.error = "Harap isi bidang ini!!"
                        binding.edtKeterangan.requestFocus()

                    }
                    true
                }

                else -> false
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)

        menuLogout.isVisible = false
        menuSave?.isVisible = true // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu


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
                    binding.apply {
                        binding.loadingInclude.loading.visibility = View.GONE
                        scrollView2.visibility = View.VISIBLE
                    }
                    val response = it.data!!
                    val messages = response.data.messages

                    if (messages != null) {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage(messages)
                            .setNegativeButton("Ya") { dialog, _ ->
                                dialog.cancel()
                            }
                        val alert = builder.create()
                        alert.show()
                    }
                    requireActivity().onBackPressed()
                }

                is NetworkResult.Loading -> {
                    binding.apply {
                        scrollView2.visibility = View.GONE
                        binding.loadingInclude.loading.visibility = View.VISIBLE
                    }
                }

                is NetworkResult.Error -> {
                    binding.apply {
                        scrollView2.visibility = View.VISIBLE
                        binding.loadingInclude.loading.visibility = View.GONE
                    }
                    handleApiError(it.message)
                }
            }
        }
    }

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            toolbar.toolbar.visibility = View.VISIBLE
            scrollView2.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            scrollView2.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }


}