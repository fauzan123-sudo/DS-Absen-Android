package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.LemburAdapter
import com.example.dsmabsen.adapter.SallaryAdapter
import com.example.dsmabsen.databinding.FragmentLemburBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.AttendanceViewModel
import com.example.dsmabsen.ui.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class LemburFragment : BaseFragment<FragmentLemburBinding>(FragmentLemburBinding::inflate) {

    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var adapter: LemburAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            val savedUser = Paper.book().read<DataX>("user")

            ajukanLembur.setOnClickListener {
                findNavController().navigate(R.id.action_lemburFragment_to_pengajuanLemburFragment)
            }
            adapter = LemburAdapter(requireContext())
            recyclerView = reclembur
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)

            viewModel.requestListLembur(savedUser!!.nip)
            viewModel.getListLemburLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val response = it.data!!
                        val status = response.status
                        if (status) {
                            adapter.differ.submitList(response.data.data)
                        } else {
                            Toast.makeText(requireContext(), "Status false", Toast.LENGTH_SHORT).show()
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

    }

}