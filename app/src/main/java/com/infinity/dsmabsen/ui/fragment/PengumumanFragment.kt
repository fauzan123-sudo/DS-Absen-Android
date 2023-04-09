package com.infinity.dsmabsen.ui.fragment

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
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.adapter.PengumumanAdapter
import com.infinity.dsmabsen.databinding.FragmentPengumumanBinding
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.viewModel.PengumumanViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PengumumanFragment :
    BaseFragment<FragmentPengumumanBinding>(FragmentPengumumanBinding::inflate) {

    lateinit var adapter: PengumumanAdapter
    val viewModel: PengumumanViewModel by viewModels()
    lateinit var recyclerView: RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            viewModel.pengumumanRequest()
            viewModel.pengumumanLivedata.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        binding.loadingInclude.loading.visibility = View.GONE
                        val response = it.data!!
                        val data = response.data.data
                        if (data.isEmpty()) {
                            binding.apply {
                                imgNoData.visibility = View.VISIBLE
                                recPengumuman.visibility = View.GONE
                            }
                        } else {
                            binding.apply {
                                imgNoData.visibility = View.GONE
                                recPengumuman.visibility = View.VISIBLE
                                adapter = PengumumanAdapter(requireContext(), data)
                                recyclerView = recPengumuman
                                recyclerView.adapter = adapter
                                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                                recyclerView.setHasFixedSize(true)
                            }
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.loadingInclude.loading.visibility = View.VISIBLE
                    }

                    is NetworkResult.Error -> {
                        binding.loadingInclude.loading.visibility = View.GONE
                        handleApiError(it.message)
                    }
                }
            }
        }

        setupToolbar("Pengumuman")

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)

        menuLogout?.isVisible = false
        menuSave?.isVisible = false
        menuPlus?.isVisible = false

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                // Handle add menu item click
                Toast.makeText(context, "Add di Click", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.save -> {
                // Handle navigation button click
                Toast.makeText(context, "Save di Click", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            toolbar.toolbar.visibility = View.VISIBLE
            recPengumuman.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            recPengumuman.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }
}