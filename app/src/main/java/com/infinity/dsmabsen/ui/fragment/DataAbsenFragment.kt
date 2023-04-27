package com.infinity.dsmabsen.ui.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.adapter.AttendanceAdapter
import com.infinity.dsmabsen.databinding.FragmentDataAbsenBinding
import com.infinity.dsmabsen.helper.Constans
import com.infinity.dsmabsen.helper.Constans.IMAGE_URL
import com.infinity.dsmabsen.helper.TokenManager
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.model.DataXX
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.viewModel.AttendanceViewModel
import com.infinity.dsmabsen.ui.viewModel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import javax.inject.Inject

@AndroidEntryPoint
class DataAbsenFragment :
    BasesFragment<FragmentDataAbsenBinding>(FragmentDataAbsenBinding::inflate) {

    private val viewModel: AttendanceViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager
    private val savedUser = Paper.book().read<DataX>("user")
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private lateinit var adapter: AttendanceAdapter
    private lateinit var recyclerView: RecyclerView
    val nip = savedUser!!.nip

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {


            userProfileViewModel.profileUserRequest(nip)
            userProfileViewModel.profileUserLivedata.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val response = it.data!!.data
                        toolbarUser.namaUser.text = response.nama
                        toolbarUser.jabatan.text = response.jabatan
                        Glide.with(requireContext())
                            .load(response.foto)
                            .into(toolbarUser.imageUser)
                    }

                    is NetworkResult.Loading -> {
                        loadingInclude.loading.visibility = View.VISIBLE
                    }

                    is NetworkResult.Error -> {
                        handleApiError(it.message)
                    }
                }
            }

//            val savedUser = Paper.book().read<DataX>("user")
            viewModel.attendanceHistoryRequest2(nip)
            viewModel.attendanceHistoryLiveData2.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        loadingInclude.loading.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
                        val attendance = it.data!!.data
                        Log.d("data_absen", attendance.toString())
                        if (attendance.isEmpty()) {
                            binding.recAttendance.visibility = View.GONE
                            binding.imgNoData.visibility = View.VISIBLE
                        } else {
                            adapter =
                                AttendanceAdapter(requireContext(), attendance) { data ->
                                    val action =
                                        DataAbsenFragmentDirections.actionDataAbsenFragmentToDetailDataAbsenFragment(
                                            data
                                        )
                                    findNavController().navigate(action)
                                }
                            recyclerView = recAttendance
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(requireContext())
                            recyclerView.setHasFixedSize(true)
                            binding.recAttendance.visibility = View.VISIBLE
                            binding.imgNoData.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Loading -> {
                        loadingInclude.loading.visibility = View.VISIBLE
                        scrollView.visibility = View.GONE
                    }

                    is NetworkResult.Error -> {
                        loadingInclude.loading.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Set Adabter Error", Toast.LENGTH_SHORT)
                        handleApiError(it.message)
                    }
                }
            }

            viewModel.attendanceTotalRequest(nip)
            viewModel.totalAttendanceLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        loadingInclude.loading.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
                        val data = it.data!!.data
//                        binding.imgNoData1.visibility = View.GONE
                        binding.pieChart.visibility = View.VISIBLE
                        if (data.masuk == 0 && data.izin == 0 && data.telat == 0 && data.alfa == 0) {
                            binding.pieChart.visibility = View.GONE
                            binding.imgNoData1.visibility = View.VISIBLE
                        } else {
                            binding.pieChart.visibility = View.VISIBLE
                            binding.imgNoData1.visibility = View.VISIBLE
                            setPieChart(data)
                        }

                    }

                    is NetworkResult.Loading -> {
                        loadingInclude.loading.visibility = View.VISIBLE
                        scrollView.visibility = View.GONE
                        binding.imgNoData1.visibility = View.GONE
                        binding.pieChart.visibility = View.GONE
                    }

                    is NetworkResult.Error -> {
                        loadingInclude.loading.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
                        binding.imgNoData1.visibility = View.VISIBLE
                        binding.pieChart.visibility = View.GONE
                        handleApiError(it.message)
                    }
                }
            }
        }
    }

    private fun FragmentDataAbsenBinding.setPieChart(data: DataXX) {
        val pieEntries = ArrayList<PieEntry>()
        Log.d("data absen", data.toString())
        pieEntries.add(PieEntry(data.masuk.toFloat(), "Masuk"))
        pieEntries.add(PieEntry(data.izin.toFloat(), "Izin"))
        pieEntries.add(PieEntry(data.alfa.toFloat(), "Alfa"))
        pieEntries.add(PieEntry(data.telat.toFloat(), "Telat"))

        val colors = ArrayList<Int>()
        colors.add(ContextCompat.getColor(requireContext(), R.color._success))
        colors.add(ContextCompat.getColor(requireContext(), R.color._primary))
        colors.add(ContextCompat.getColor(requireContext(), R.color._danger))
        colors.add(ContextCompat.getColor(requireContext(), R.color._secondary))

        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.colors = colors
        pieDataSet.sliceSpace = 1f
        val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
        pieDataSet.valueTypeface = boldTypeface

        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)
        pieData.setValueTextColor(Color.WHITE)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(14f)

//        Atur deskripsi chart
        pieChart.description.isEnabled = false
        pieChart.setEntryLabelTextSize(134f)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f

        // on below line we are setting center text
        pieChart.setDrawCenterText(true)

        pieChart.setDrawEntryLabels(false)

        // Atur data yang akan ditampilkan pada chart
        pieChart.data = pieData
        pieChart.invalidate()

        //             set value formatter
        pieDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }
    }
//    override fun onConnectionAvailable() {
//        super.onConnectionAvailable()
//        binding.apply {
//            toolbarUser.visibility = View.GONE
//            rcycleview.visibility = View.VISIBLE
//            noInternetConnection.ivNoConnection.visibility = View.GONE
//        }
//    }
//
//    override fun onConnectionLost() {
//        super.onConnectionLost()
//        binding.apply {
//            toolbar.visibility = View.GONE
//            rcycleview.visibility = View.GONE
//            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
//        }
//    }

}