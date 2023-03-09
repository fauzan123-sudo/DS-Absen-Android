package com.example.dsmabsen.ui.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.AttendanceAdapter
import com.example.dsmabsen.databinding.FragmentDataAbsenBinding
import com.example.dsmabsen.helper.TokenManager
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.model.DataXX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.AttendanceViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import javax.inject.Inject

@AndroidEntryPoint
class DataAbsenFragment :
    BaseFragment<FragmentDataAbsenBinding>(FragmentDataAbsenBinding::inflate) {

    private val viewModel: AttendanceViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var adapter: AttendanceAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            adapter = AttendanceAdapter(requireContext())
            recyclerView = recAttendance
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)

            val savedUser = Paper.book().read<DataX>("user")
            viewModel.attendanceHistoryRequest(savedUser!!.nip)
            viewModel.attendanceHistoryLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val attendance = it.data!!.data
                        Toast.makeText(requireContext(), "$attendance disini", Toast.LENGTH_SHORT).show()
                        adapter.differ.submitList(attendance)

                    }

                    is NetworkResult.Loading -> {
//                        spinKit.isVisible = true
                        Toast.makeText(requireContext(), "Sedang proses", Toast.LENGTH_SHORT).show()
                    }

                    is NetworkResult.Error -> {
                        handleApiError(it.message)
                    }
                }
            }

            viewModel.attendanceTotalRequest(savedUser.nip)
            viewModel.totalAttendanceLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val data = it.data!!.data
                        setPieChart(data)
//                        val entries = listOf(
//                            PieEntry(data.masuk.toFloat(), "Masuk"),
//                            PieEntry(data.telat.toFloat(), "Telat"),
//                            PieEntry(data.alfa.toFloat(), "Alfa"),
//                            PieEntry(data.izin.toFloat(), "Izin")
//                        )
//
//                        val colors = listOf(
//                            Color.parseColor("#FFC107"),
//                            Color.parseColor("#F44336"),
//                            Color.parseColor("#9E9E9E"),
//                            Color.parseColor("#4CAF50")
//                        )
//
//                        // Atur warna untuk setiap data
////                        val colors = ArrayList<Int>()
////                        colors.add(ContextCompat.getColor(requireContext(), R.color._success))
////                        colors.add(ContextCompat.getColor(requireContext(), R.color._primary))
////                        colors.add(ContextCompat.getColor(requireContext(), R.color._danger))
////                        colors.add(ContextCompat.getColor(requireContext(), R.color._secondary))
//
//                        // Buat objek PieDataSet dengan data dan warna yang sudah ditentukan
//                        val pieDataSet = PieDataSet(entries, "")
//                        pieDataSet.colors = colors
//                        pieDataSet.valueTextColor = Color.WHITE
//
//
//                        // Buat objek PieData dan atur dataset yang sudah dibuat sebelumnya
//                        val pieData = PieData(pieDataSet)
//                        pieData.setValueTextColor(Color.WHITE)
//                        pieData.setValueTextSize(15f)
//                        pieData.setValueTypeface(Typeface.DEFAULT_BOLD)
//
//                        val dataSet = PieDataSet(entries, "")
//                        dataSet.colors = colors
//                        dataSet.valueTextSize = 14f
//
//                        val dataPie = PieData(dataSet)
//                        pieChart.data = dataPie
//                        pieChart.setDrawEntryLabels(false)
//                        pieChart.description.isEnabled = false
//
//                        pieChart.invalidate()


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

    private fun FragmentDataAbsenBinding.setPieChart(data: DataXX) {
        val pieEntries = ArrayList<PieEntry>()
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

}