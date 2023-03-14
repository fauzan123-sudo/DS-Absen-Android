package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.ReimbursementAdapter
import com.example.dsmabsen.databinding.FragmentReimbursementBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.ReimbursementViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class ReimbursementFragment :
    BaseFragment<FragmentReimbursementBinding>(FragmentReimbursementBinding::inflate) {

    val viewModel: ReimbursementViewModel by viewModels()
    private lateinit var adapter: ReimbursementAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        setupToolbar("Reimbursement")
        val savedUser = Paper.book().read<DataX>("user")
        with(binding) {
            loadingInclude.loading.visibility = View.VISIBLE
            imgNoData.isVisible = false
            adapter = ReimbursementAdapter(requireContext())
            recyclerView = recReimbursement
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)

            viewModel.requestReimbursement(savedUser!!.nip)
            viewModel.getReimbursementLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        loadingInclude.loading.visibility = View.GONE
                        val response = it.data!!
                        val status = response.status
                        if (status) {
                            if(response.data.data.isEmpty()){
                                recReimbursement.isVisible = false
                                imgNoData.isVisible = true
                            }else{
                                adapter.differ.submitList(response.data.data)
                                recReimbursement.isVisible = true
                                imgNoData.isVisible = false
                            }
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.VISIBLE
                            recReimbursement.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            recReimbursement.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }
                }

            }
        }

        super.onViewCreated(view, savedInstanceState)

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    // Handle add menu item click
                    findNavController().navigate(R.id.action_reimbursementFragment_to_pengajuanReimbursementFragment)
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