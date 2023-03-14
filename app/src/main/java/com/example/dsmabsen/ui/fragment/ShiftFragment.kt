package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.SallaryAdapter
import com.example.dsmabsen.adapter.ShiftAdapter
import com.example.dsmabsen.databinding.FragmentShiftBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.AttendanceViewModel
import com.example.dsmabsen.ui.viewModel.ShiftViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class ShiftFragment : BaseFragment<FragmentShiftBinding>(FragmentShiftBinding::inflate) {

    private val viewModel: ShiftViewModel by viewModels()
    private lateinit var adapter: ShiftAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedUser = Paper.book().read<DataX>("user")

        with(binding) {
            loadingInclude.loading.visibility = View.VISIBLE
            imgNoData.isVisible = false


            adapter = ShiftAdapter(requireContext())
            recyclerView = recyclerShift
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)

            viewModel.requestShift(savedUser!!.nip)
            viewModel.getShiftLiveData.observe(viewLifecycleOwner) {

                when (it) {
                    is NetworkResult.Success -> {
                        loadingInclude.loading.visibility = View.GONE
                        val response = it.data!!
                        val status = response.status
                        if (status) {
                            if(response.data.data.isEmpty()){
                                recyclerShift.isVisible = false
                                imgNoData.isVisible = true
                            }else{
                                adapter.differ.submitList(response.data.data)
                                recyclerShift.isVisible = true
                                imgNoData.isVisible = false
                            }
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.VISIBLE
                            recyclerShift.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            recyclerShift.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }
                }
            }
        }
        setHasOptionsMenu(true)
        setupToolbar("Shift")
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    findNavController().navigate(R.id.action_shiftFragment_to_pengajuanShiftFragment)
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
        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = true // menyembunyikan menu tertentu

    }

}