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
        setHasOptionsMenu(true)
        setupToolbar("Lembur")
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    findNavController().navigate(R.id.action_lemburFragment_to_pengajuanLemburFragment)

                    true
                }

                else -> false
            }
        }
        with(binding) {
            loadingInclude.loading.visibility = View.VISIBLE
            imgNoData.isVisible = false

            val savedUser = Paper.book().read<DataX>("user")

//            ajukanLembur.setOnClickListener {
//                findNavController().navigate(R.id.action_lemburFragment_to_pengajuanLemburFragment)
//            }
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
                        loadingInclude.loading.visibility = View.GONE
                        if (status) {
                            if(response.data.data.isEmpty()){
                                reclembur.isVisible = false
                                imgNoData.isVisible = true
                            }else{
                                adapter.differ.submitList(response.data.data)
                                reclembur.isVisible = true
                                imgNoData.isVisible = false
                            }
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            binding.loadingInclude.loading.visibility = View.VISIBLE
                            reclembur.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            binding.loadingInclude.loading.visibility = View.GONE
                            reclembur.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }
                }
            }

        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)

        menuLogout?.isVisible = false // menyembunyikan menu tertentu
        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = true // menyembunyikan menu tertentu


    }

}