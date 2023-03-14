package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.AttendanceAdapter
import com.example.dsmabsen.adapter.PerizinanAdapter
import com.example.dsmabsen.databinding.FragmentPerizinanBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.PerizinanViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class MenuPerizinanFragment :
    BaseFragment<FragmentPerizinanBinding>(FragmentPerizinanBinding::inflate) {

    private val viewModel: PerizinanViewModel by viewModels()
    private lateinit var adapter: PerizinanAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupToolbar("Perizinan")
        val savedUser = Paper.book().read<DataX>("user")
        with(binding) {
            loadingInclude.loading.visibility = View.VISIBLE
            imgNoData.isVisible = false

            adapter = PerizinanAdapter(requireContext())
            recyclerView = recPerizinan
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)
            viewModel.requestPerizinan(savedUser!!.nip)
            viewModel.perizinanLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        loadingInclude.loading.visibility = View.GONE
                        val data = it.data!!
                        val status = data.status
                        if (status) {
                            if (data.data.data.isEmpty()) {
                                recPerizinan.isVisible = false
                                imgNoData.isVisible = true
                            } else {
                                adapter.differ.submitList(data.data.data)
                                recPerizinan.isVisible = true
                                imgNoData.isVisible = false
                            }
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.VISIBLE
                            recPerizinan.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            recPerizinan.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }
                }
            }
        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    // Handle add menu item click
                    findNavController().navigate(R.id.action_menuPerizinanFragment_to_formPerizinanFragment)
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

        menuSave?.isVisible = false
        menuPlus?.isVisible = true
        menuLogout?.isVisible = false


    }

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            toolbar.toolbar.visibility = View.VISIBLE
            recPerizinan.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            recPerizinan.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }


}