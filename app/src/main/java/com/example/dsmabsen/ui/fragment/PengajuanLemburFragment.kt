package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentPengajuanLemburBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.AttendanceViewModel
import com.example.dsmabsen.ui.viewModel.HomeViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PengajuanLemburFragment :
    BaseFragment<FragmentPengajuanLemburBinding>(FragmentPengajuanLemburBinding::inflate) {

    private val viewModel: AttendanceViewModel by viewModels()
    val savedUser = Paper.book().read<DataX>("user")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            tanggalPenggajuan.setOnClickListener {
                openCalendar()
            }

            jamMulai.setOnClickListener {
                openTimePickerFrom()
            }

            jamSelesai.setOnClickListener {
                openTimePickerUntil()
            }

        }
        setHasOptionsMenu(true)
        setupToolbar("Ajukan Lembur")

    }

    private fun saveLembur() {
        viewModel.requestPengajuanLembur(
            savedUser!!.nip,
            binding.jamMulai.text.toString(),
            binding.jamSelesai.text.toString(),
            "",
            binding.tanggalPenggajuan.text.toString(),
            binding.keteranganPenggajuan.text.toString()
        )
        viewModel.pengajuanLemburLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val response = it.data!!
                    val status = response.status
                    val message = response.data.messages
                    binding.apply {
                        binding.loadingInclude.loading.visibility = View.GONE
                        scrollView2.visibility = View.VISIBLE
                    }
                    requireActivity().onBackPressed()

                }

                is NetworkResult.Loading -> {
                    binding.apply {
                        binding.loadingInclude.loading.visibility = View.VISIBLE
                        scrollView2.visibility = View.GONE
                    }
                }

                is NetworkResult.Error -> {
                    binding.apply {
                        binding.loadingInclude.loading.visibility = View.GONE
                        scrollView2.visibility = View.VISIBLE
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

        menuSave?.isVisible = true // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu

        val item = menu.findItem(R.id.save)
        item.setActionView(R.layout.item_menu_toolbar)

        val actionView = item.actionView
        val btnSimpan = actionView?.findViewById<TextView>(R.id.textSimpan)
        btnSimpan?.setOnClickListener {
            var i = 0;
            if(binding.jamMulai.text.isEmpty()){
                binding.jamMulai.error = "Harap isi bidang ini!!"
                binding.jamMulai.requestFocus()
                i++
            }
            if (binding.jamSelesai.text.isEmpty()) {
                binding.jamSelesai.error = "Harap isi bidang ini!!"
                binding.jamSelesai.requestFocus()
                i++

            }
            if(binding.tanggalPenggajuan.text.isEmpty()){
                binding.tanggalPenggajuan.error = "Harap isi bidang ini!!"
                binding.tanggalPenggajuan.requestFocus()
                i++

            }
            if (binding.keteranganPenggajuan.text.isEmpty()) {
                binding.keteranganPenggajuan.error = "Harap isi bidang ini!!"
                binding.keteranganPenggajuan.requestFocus()
                i++
            }
            if(i == 0){
                saveLembur()
            }

        }

    }

    private fun openCalendar() {
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("")
            .build()
        datePicker.show(childFragmentManager, "DatePicker")

        datePicker.addOnPositiveButtonClickListener {
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            val date = dateFormatter.format(Date(it))
            binding.tanggalPenggajuan.text = date
//            Toast.makeText(requireContext(), "$date is selected", Toast.LENGTH_LONG).show()

        }

        datePicker.addOnNegativeButtonClickListener {
            Log.d("TAG", "openCalendar: negative button")
        }

        datePicker.addOnCancelListener {
            Log.d("TAG", "openCalendar: Date Picker Cancelled ")
        }
    }

    private fun openTimePickerFrom() {
        val isSystem24Hour = DateFormat.is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(8)
            .setMinute(0)
            .setTitleText("")
            .build()
        picker.show(childFragmentManager, "MyTag")

        picker.addOnPositiveButtonClickListener {
            var h = picker.hour
            var m = picker.minute
            if (h < 10 || m < 10) {
                h = String.format("0%02d", h).toInt()
            } else {
                h = String.format("%d", h).toInt()
            }
            m = String.format("%02d", m).toInt()

            var fixH = h.toString()
            var fixM = m.toString()
            if (h < 10) {
                fixH = "0$fixH"
            }
            if (m < 10) {
                fixM = "0$fixM"
            }
            binding.jamMulai.text = "$fixH:$fixM"
        }
    }

    private fun openTimePickerUntil() {
        val isSystem24Hour = DateFormat.is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(8)
            .setMinute(0)
            .setTitleText("")
            .build()
        picker.show(childFragmentManager, "MyTag")

        picker.addOnPositiveButtonClickListener {
            var h = picker.hour
            var m = picker.minute
            if (h < 10) {
                h = String.format("%02d", h).toInt()
            } else {
                h = String.format("%d", h).toInt()
            }
            m = String.format("%02d", m).toInt()

            var fixH = h.toString()
            var fixM = m.toString()
            if (h < 10) {
                fixH = "0$fixH"
            }
            if (m < 10) {
                fixM = "0$fixM"
            }

            binding.jamSelesai.text = "$fixH:$fixM"
        }
    }


}