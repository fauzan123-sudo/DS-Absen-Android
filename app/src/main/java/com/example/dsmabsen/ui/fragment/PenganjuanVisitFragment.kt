package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import com.example.dsmabsen.adapter.SpinnerShiftAdapter
import com.example.dsmabsen.adapter.SpinnerVisitAdapter
import com.example.dsmabsen.databinding.FragmentPenganjuanVisitBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.VisitViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class PenganjuanVisitFragment :
    BaseFragment<FragmentPenganjuanVisitBinding>(FragmentPenganjuanVisitBinding::inflate) {

    private lateinit var adapter: SpinnerVisitAdapter

    private var selectedJenisVisit: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX? = null
    var kodeVisit: String? = null
    val viewModel: VisitViewModel by viewModels()
    val savedUser = Paper.book().read<DataX>("user")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            viewModel.spinnerVisitRequest()
            viewModel.spinnerVisitLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val response = it.data!!
                        val status = response.status
                        val data = response.data
                        if (status) {
                            val list: ArrayList<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX> = ArrayList()
                            data.map {
                                list.add(
                                    DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX(
                                        it.created_at,
                                        it.id,
                                        it.jarak,
                                        it.kode_visit,
                                        it.kordinat,
                                        "",
                                        "",
                                        "",
                                        ""
                                    )
                                )
                            }

                            val adapter = SpinnerVisitAdapter(requireContext(), list)
                            spinnerShift.adapter = adapter
                            spinnerShift.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        val clickedItem: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX =
                                            parent?.getItemAtPosition(position) as DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                                        val idSpinner = clickedItem.kode_visit
//                                        val koCuti = clickedItem.kode_shift
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
                        loadingInclude.loading.visibility = View.VISIBLE
                        scrollView2.visibility = View.GONE
                    }

                    is NetworkResult.Error -> {
                        handleApiError(it.message)
                    }
                }
            }
        }

    }

}