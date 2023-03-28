package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.VisitAdapter
import com.example.dsmabsen.databinding.FragmentVisitBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.VisitViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class VisitFragment : BaseFragment<FragmentVisitBinding>(FragmentVisitBinding::inflate) {

    private val viewModel: VisitViewModel by viewModels()
    private lateinit var adapter: VisitAdapter
    private lateinit var recyclerView: RecyclerView
    val savedUser = Paper.book().read<DataX>("user")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            loadingInclude.loading.visibility = View.VISIBLE
            imgNoData.isVisible = false

            addVisited.setOnClickListener {
                findNavController().navigate(R.id.action_visitFragment2_to_penganjuanVisitFragment)
            }

            adapter = VisitAdapter(requireContext())
            recyclerView = recVisit
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)

            viewModel.visitRequest(savedUser!!.nip)
            viewModel.visitLiveData.observe(viewLifecycleOwner) {

                when (it) {
                    is NetworkResult.Success -> {
                        loadingInclude.loading.visibility = View.GONE
                        val response = it.data!!
                        val status = response.status
                        if (status) {
                            if (response.data.isEmpty()) {
                                recVisit.isVisible = false
                                imgNoData.isVisible = true
                            } else {
                                adapter.differ.submitList(response.data)
                                recVisit.isVisible = true
                                imgNoData.isVisible = false
                            }
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.VISIBLE
                            recVisit.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            recVisit.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }
                }
            }
        }

        setHasOptionsMenu(true)
        hideToolbar()
        toolbar.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)

        menuLogout.isVisible = false
        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu
    }

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            toolbar.toolbar.visibility = View.VISIBLE
            recVisit.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            recVisit.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }
}


