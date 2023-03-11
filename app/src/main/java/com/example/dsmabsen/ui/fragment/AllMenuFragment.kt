package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentAllMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMenuFragment :BaseFragment<FragmentAllMenuBinding>(FragmentAllMenuBinding::inflate){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar("Semua Menu")
        binding.btnReimbursement.setOnClickListener {
            findNavController().navigate(R.id.action_allMenuFragment_to_reimbursementFragment)
        }

        view.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.add -> {
                        // Handle add menu item click
                        Toast.makeText(context, "Add di Click", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.save -> {
                        // Handle navigation button click
                        Toast.makeText(context, "Save di Click", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)

        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)

        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu

        super.onCreateOptionsMenu(menu, inflater)
    }
}