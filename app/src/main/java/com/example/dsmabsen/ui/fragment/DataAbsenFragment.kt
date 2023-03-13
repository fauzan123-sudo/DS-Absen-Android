package com.example.dsmabsen.ui.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.AttendanceAdapter
import com.example.dsmabsen.databinding.FragmentDataAbsenBinding
import com.example.dsmabsen.helper.Constans
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
    private val savedUser = Paper.book().read<DataX>("user")

    private lateinit var adapter: AttendanceAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideToolbar()

        binding.apply {
            namaUser.text = savedUser!!.name
            jabatan.text = savedUser!!.nama_jabatan
            Glide.with(requireContext())
                .load(Constans.IMAGE_URL + savedUser!!.image)
                .into(imageUser)

            imageUser.setOnClickListener {
                findNavController().navigate(R.id.action_dataAbsenFragment_to_profileFragment)
            }

            adapter = AttendanceAdapter(requireContext())
            recyclerView = recAttendance
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)

            val savedUser = Paper.book().read<DataX>("user")
            viewModel.attendanceHistoryRequest2(savedUser!!.nip)
            viewModel.attendanceHistoryLiveData2.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        loading.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
                        val attendance = it.data!!.data
                        Log.d("data_absen", attendance.toString())
                        if (attendance.isEmpty()) {
                            binding.recAttendance.visibility = View.GONE
                            binding.imgNoData.visibility = View.VISIBLE
                        } else {
                            adapter.differ.submitList(attendance)
                            binding.recAttendance.visibility = View.VISIBLE
                            binding.imgNoData.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Loading -> {
                        loading.visibility = View.VISIBLE
                        scrollView.visibility = View.GONE
                    }

                    is NetworkResult.Error -> {
                        loading.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Set Adabter Error", Toast.LENGTH_SHORT)
                        handleApiError(it.message)
                    }
                }
            }

            viewModel.attendanceTotalRequest(savedUser.nip)
            viewModel.totalAttendanceLiveData.observe(viewLifecycleOwner) {

                when (it) {
                    is NetworkResult.Success -> {
                        loading.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
                        val data = it.data!!.data
                        setPieChart(data)
                    }

                    is NetworkResult.Loading -> {
                        loading.visibility = View.VISIBLE
                        scrollView.visibility = View.GONE
                    }

                    is NetworkResult.Error -> {
                        loading.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
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