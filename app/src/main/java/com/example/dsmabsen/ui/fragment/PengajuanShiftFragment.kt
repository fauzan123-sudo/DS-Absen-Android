package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.SpinnerShiftAdapter
import com.example.dsmabsen.databinding.FragmentPengajuanShiftBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.ShiftViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import kotlinx.coroutines.NonDisposableHandle.parent

@AndroidEntryPoint
class PengajuanShiftFragment :
    BaseFragment<FragmentPengajuanShiftBinding>(FragmentPengajuanShiftBinding::inflate) {
    val savedUser = Paper.book().read<DataX>("user")
    val viewModel: ShiftViewModel by viewModels()

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
                                        Log.d("idSpinner", idSpinner)
                                        Toast.makeText(
                                            requireContext(),
                                            idSpinner,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                        Log.d("select", "onNothingSelected: ")
                                    }

                                }

                        } else {

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
        setHasOptionsMenu(true)
        setupToolbar("Ajukan Shift")

    }

    private fun saveShift(savedUser: DataX?) {
        viewModel.requestShiftPengajuan(
            savedUser!!.nip,
            "1",
            "",
            binding.edtKeterangan.toString()
        )
        viewModel.pengajuanShiftLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val response = it.data!!
                    val status = response.status
                    val statusPengajuan = response.data.status
                    if (status == statusPengajuan) {
                        Toast.makeText(
                            requireContext(),
                            "Anda berhasil mengajukan Shift",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            statusPengajuan.toString(),
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
            // your code here
            saveShift(savedUser)

        }

    }

}