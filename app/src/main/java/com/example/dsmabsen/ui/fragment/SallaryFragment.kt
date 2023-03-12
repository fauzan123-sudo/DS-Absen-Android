package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.AttendanceAdapter
import com.example.dsmabsen.adapter.SallaryAdapter
import com.example.dsmabsen.databinding.FragmentSallaryBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.AttendanceViewModel
import com.example.dsmabsen.ui.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class SallaryFragment : BaseFragment<FragmentSallaryBinding>(FragmentSallaryBinding::inflate) {


    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var adapter: SallaryAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedUser = Paper.book().read<DataX>("user")

        with(binding) {
            adapter = SallaryAdapter(requireContext())
            recyclerView = recSallary
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)

            viewModel.requestSallary(savedUser!!.nip)
            viewModel.getSallaryLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val response = it.data!!
                        val status = response.status
                        if (status) {
                            adapter.differ.submitList(response.data)
                        } else {
                            Toast.makeText(requireContext(), "status False", Toast.LENGTH_SHORT)
                                .show()
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
        setHasOptionsMenu(false)
        setupToolbar("Gaji")


    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)

        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu

        val item = menu.findItem(R.id.save)
        item.setActionView(R.layout.item_menu_toolbar)

        val actionView = item.actionView
        val btnSimpan = actionView?.findViewById<TextView>(R.id.textSimpan)
        btnSimpan?.setOnClickListener {
            // your code here
//            saveReimbursement(savedUser)

        }

    }

}