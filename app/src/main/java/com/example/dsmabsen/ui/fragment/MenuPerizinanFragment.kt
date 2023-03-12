package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.AttendanceAdapter
import com.example.dsmabsen.adapter.PerizinanAdapter
import com.example.dsmabsen.databinding.FragmentMenuPerizinanBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.PerizinanViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class MenuPerizinanFragment :
    BaseFragment<FragmentMenuPerizinanBinding>(FragmentMenuPerizinanBinding::inflate) {

    private val viewModel: PerizinanViewModel by viewModels()
    private lateinit var adapter: PerizinanAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupToolbar("Perizinan")
        val savedUser = Paper.book().read<DataX>("user")
        with(binding) {

//            rvPlusReimbursement.setOnClickListener {
//                findNavController().navigate(R.id.action_menuPerizinanFragment_to_formPerizinanFragment)
//            }

            adapter = PerizinanAdapter(requireContext())
            recyclerView = recPerizinan
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)
            viewModel.requestPerizinan(savedUser!!.nip)
            viewModel.perizinanLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val data = it.data!!
                        val status = data.status
                        if (status) {
                            adapter.differ.submitList(data.data.data)
                        } else {
                            Toast.makeText(requireContext(), "data perizinan salah", Toast.LENGTH_SHORT).show()
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

        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = true // menyembunyikan menu tertentu
    }

}