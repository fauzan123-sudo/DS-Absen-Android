package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.SpinnerShiftAdapter
import com.example.dsmabsen.databinding.FragmentPengajuanShiftBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.ShiftViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class PengajuanShiftFragment :
    BaseFragment<FragmentPengajuanShiftBinding>(FragmentPengajuanShiftBinding::inflate) {
    val savedUser = Paper.book().read<DataX>("user")
    val viewModel: ShiftViewModel by viewModels()

    private var selectedShift: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {

            viewModel.requestSpinnerShift()
            viewModel.spinnerShiftLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val response = it.data!!
                        val status = response.status
                        val data = response.data
                        if (status) {
                            val list: ArrayList<DataXXXXXXXXXXXXXXXXXXXXXXX> = ArrayList()
                            data.map {
                                list.add(
                                    DataXXXXXXXXXXXXXXXXXXXXXXX(
                                        it.kode_shift,
                                        it.label,
                                        it.value
                                    )
                                )
                            }

                            val adapter = SpinnerShiftAdapter(requireContext(), list)
                            spinnerShift.adapter = adapter
                            spinnerShift.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        val clickedItem: DataXXXXXXXXXXXXXXXXXXXXXXX =
                                            parent?.getItemAtPosition(position) as DataXXXXXXXXXXXXXXXXXXXXXXX
                                        val idSpinner = clickedItem.label
                                        val koCuti = clickedItem.kode_shift
                                        selectedShift = koCuti
                                        Log.d("idSpinner", idSpinner)
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                        Log.d("select", "onNothingSelected: ")
                                    }

                                }
                            binding.apply {
                                loadingInclude.loading.visibility = View.GONE
                                scrollView2.visibility = View.VISIBLE
                            }
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.VISIBLE
                            scrollView2.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            scrollView2.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }
                }
            }


        }

        setHasOptionsMenu(true)
        setupToolbar("Ajukan Shift")
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save -> {
                    val keterangan = binding.edtKeterangan.text.toString()
                    if (keterangan.isEmpty()) {
                        binding.edtKeterangan.error = "Harap isi bidang ini!!"
                        binding.edtKeterangan.requestFocus()
                    } else {
                        saveShift(savedUser)
                    }
                    true
                }

                else -> false
            }
        }
    }


    private fun saveShift(savedUser: DataX?) {
        viewModel.requestShiftPengajuan(
            savedUser!!.nip,
            selectedShift!!,
            "",
            binding.edtKeterangan.text.toString()
        )
        viewModel.pengajuanShiftLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    binding.apply {
                        binding.loadingInclude.loading.visibility = View.GONE
                        scrollView2.visibility = View.VISIBLE
                    }
                    val response = it.data!!
                    val messages = response.data.messages
                    if(messages != null){
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
                    requireActivity().onBackPressed()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)

        menuLogout.isVisible = false
        menuSave?.isVisible = true // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu
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