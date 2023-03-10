package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.SpinnerAdapter
import com.example.dsmabsen.databinding.FragmentFormPerizinanBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.PerizinanViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FormPerizinanFragment :
    BaseFragment<FragmentFormPerizinanBinding>(FragmentFormPerizinanBinding::inflate) {

    private val viewModel: PerizinanViewModel by viewModels()
    val savedUser = Paper.book().read<DataX>("user")
    private var kode_perizinan: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupToolbar("Ajukan Perizinan")
        view.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.save -> {
                        savePerizinan(savedUser)
                        true
                    }

                    else -> false
                }
            }
        }
        with(binding) {

            etTanggalMulai.setOnClickListener {
                getCalendarStart()
            }

            etTanggalSelesai.setOnClickListener {
                getCalendarEnd()
            }
            viewModel.requestgetSpinner()
            viewModel.getSpinnerLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val data = it.data!!
                        val status = data.status
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            scrollView.visibility = View.VISIBLE
                        }
                        if (status) {
                            val list: ArrayList<DataXXXXXXXXXXXXX> = ArrayList()
                            data.data.map {
                                list.add(
                                    DataXXXXXXXXXXXXX(
                                        it.kode_cuti,
                                        it.label,
                                        it.value
                                    )
                                )
                            }
                            val spinnerAdapter = SpinnerAdapter(requireContext(), list)
                            spinnerJenisIzin.adapter = spinnerAdapter
                            spinnerJenisIzin.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        val clickedItem: DataXXXXXXXXXXXXX =
                                            parent?.getItemAtPosition(position) as DataXXXXXXXXXXXXX
                                        val idSpinner = clickedItem.kode_cuti
                                        kode_perizinan = idSpinner
                                        Log.d("idSpinner", idSpinner)
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                        Log.d("nothing", "No")
                                    }

                                }
                        } else {
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.VISIBLE
                            scrollView.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            scrollView.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }

                    else -> ""
                }
            }

        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            var i = 0
            when (menuItem.itemId) {
                R.id.save -> {

                    if (binding.etTanggalMulai.text.isEmpty()) {
                        binding.etTanggalMulai.error = "Harap isi bidang ini!!"
                        binding.etTanggalMulai.requestFocus()
                        i++

                    }
                    if (binding.etTanggalSelesai.text.isEmpty()) {
                        binding.etTanggalSelesai.error = "Harap isi bidang ini!!"
                        binding.etTanggalSelesai.requestFocus()
                        i++

                    }
                    if (binding.etKeterangan.text.isEmpty()) {
                        binding.etKeterangan.error = "Harap isi bidang ini!!"
                        binding.etKeterangan.requestFocus()
                        i++
                    }
                    if (i == 0) {
                        savePerizinan(savedUser)
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

    private fun getCalendarEnd() {
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("")
            .build()
        datePicker.show(childFragmentManager, "DatePicker")

        datePicker.addOnPositiveButtonClickListener {
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            val date = dateFormatter.format(Date(it))
            binding.etTanggalSelesai.text = date

        }

        datePicker.addOnNegativeButtonClickListener {
            Log.d("TAG", "getCalendarEnd: negetive button")
        }

        datePicker.addOnCancelListener {
            Log.d("TAG", "getCalendarEnd: calnceled")
        }
    }

    private fun getCalendarStart() {
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("")
            .build()
        datePicker.show(childFragmentManager, "DatePicker")

        datePicker.addOnPositiveButtonClickListener {
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            val date = dateFormatter.format(Date(it))
            binding.etTanggalMulai.text = date

        }

        datePicker.addOnNegativeButtonClickListener {
            Log.d("TAG", "getCalendarStart:Negative button")
        }

        datePicker.addOnCancelListener {
            Log.d("TAG", "getCalendarStart:Cancel button")
        }

    }

    private fun savePerizinan(savedUser: DataX?) {
        viewModel.requestSendPermission(
            savedUser!!.nip,
            kode_perizinan!!,
            binding.etTanggalMulai.text.toString(),
            binding.etTanggalSelesai.text.toString(),
            "",
            binding.etKeterangan.text.toString()
        )

        viewModel.sendPermissionLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    binding.apply {
                        loadingInclude.loading.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
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
                        loadingInclude.loading.visibility = View.VISIBLE
                        scrollView.visibility = View.GONE
                    }
                }

                is NetworkResult.Error -> {
                    binding.apply {
                        loadingInclude.loading.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
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
            scrollView.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            scrollView.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }

}