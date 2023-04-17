package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.adapter.AktivitasAdapter
import com.infinity.dsmabsen.databinding.FragmentAktivitasBinding
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.viewModel.AktivitasViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class AktivitasFragment :
    BaseFragment<FragmentAktivitasBinding>(FragmentAktivitasBinding::inflate) {
    val savedUser = Paper.book().read<DataX>("user")
    val nipUser = savedUser!!.nip
    private lateinit var adapter: AktivitasAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: AktivitasViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar("Aktivitas")
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.scan -> {
                    findNavController().navigate(R.id.action_visitFragment2_to_scanFragment)
                    true
                }

                else -> false
            }
        }
        setHasOptionsMenu(true)
        viewModel.aktivitasRequest(nipUser)
        viewModel.aktivitasLiveData.observe(viewLifecycleOwner) {
            binding.loadingInclude.loading.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    val response = it.data!!
                    Log.d("sukses load data", "onViewCreated: ")
                    if (response.data.isNotEmpty()) {
                        adapter = AktivitasAdapter(requireContext(), response.data) { aktivitas ->
                            val action =
                                AktivitasFragmentDirections.actionAktivitasFragmentToDetailAktivitasFragment(
                                    aktivitas
                                )
                            findNavController().navigate(action)

                        }
                        recyclerView = binding.recAktivitas
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        recyclerView.setHasFixedSize(true)
                        binding.imgNoData.visibility = View.GONE
                    } else {
                        binding.imgNoData.visibility = View.VISIBLE
                    }
                }

                is NetworkResult.Loading -> {
                    binding.loadingInclude.loading.visibility = View.VISIBLE
                }

                is NetworkResult.Error -> {
                    handleApiError(it.message)
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)
        val menuScan = menu.findItem(R.id.scan)

        menuScan.isVisible = false
        menuLogout.isVisible = false
        menuSave?.isVisible = false
        menuPlus?.isVisible = false
    }

}