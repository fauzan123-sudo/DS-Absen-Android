package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
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
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FormPerizinanFragment :
    BaseFragment<FragmentFormPerizinanBinding>(FragmentFormPerizinanBinding::inflate) {

    private val viewModel: PerizinanViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val savedUser = Paper.book().read<DataX>("user")
            etTanggalMulai.setOnClickListener {
                getCalendarStart()
            }

            etTanggalSelesai.setOnClickListener {
                getCalendarEnd()
            }

            kirim.setOnClickListener {
                viewModel.requestSendPermission(
                    savedUser!!.nip,
                    "1",
                    etTanggalMulai.toString(),
                    etTanggalSelesai.toString(),
                    "",
                    etKeterangan.toString()
                )

                viewModel.sendPermissionLiveData.observe(viewLifecycleOwner) {
                    when (it) {
                        is NetworkResult.Success -> {
                            val response = it.data!!
                            val status = response.status

                            if (status) {
                                Toast.makeText(
                                    requireContext(),
                                    "berhasil menambhakan data",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "berhasil menambhakan data",
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
            viewModel.requestgetSpinner()
            viewModel.getSpinnerLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {

                        val data = it.data!!
                        val status = data.status

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
                                        val idSpinner = clickedItem.label
                                        Log.d("idSpinner", idSpinner)
                                        Toast.makeText(
                                            requireContext(),
                                            idSpinner,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                        Log.d("nothing", "No")
                                    }

                                }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Kendala di spinner",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    is NetworkResult.Loading -> {
                        Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
                    }

                    is NetworkResult.Error -> {
                        handleApiError(it.message)
                    }

                    else -> ""
                }
            }
        }
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
            Toast.makeText(requireContext(), "$date is selected", Toast.LENGTH_LONG).show()

        }

        datePicker.addOnNegativeButtonClickListener {
            Toast.makeText(
                requireContext(),
                "${datePicker.headerText} is cancelled",
                Toast.LENGTH_LONG
            ).show()
        }

        datePicker.addOnCancelListener {
            Toast.makeText(requireContext(), "Date Picker Cancelled", Toast.LENGTH_LONG).show()
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
            Toast.makeText(requireContext(), "$date is selected", Toast.LENGTH_LONG).show()

        }

        datePicker.addOnNegativeButtonClickListener {
            Toast.makeText(
                requireContext(),
                "${datePicker.headerText} is cancelled",
                Toast.LENGTH_LONG
            ).show()
        }

        datePicker.addOnCancelListener {
            Toast.makeText(requireContext(), "Date Picker Cancelled", Toast.LENGTH_LONG).show()
        }

    }

}