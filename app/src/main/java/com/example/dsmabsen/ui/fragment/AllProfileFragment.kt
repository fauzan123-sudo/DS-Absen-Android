package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentAllProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllProfileFragment :BaseFragment<FragmentAllProfileBinding>(FragmentAllProfileBinding::inflate){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            rvDataPribadi.setOnClickListener {
                findNavController().navigate(R.id.action_allProfileFragment_to_personalDataFragment)
            }
        }
        setHasOptionsMenu(true)
        setupToolbar("Profile")
        view.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.save -> {
                        // Handle add menu item click
//                        saveReimbursement(savedUser)
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
        val menuLogout = menu.findItem(R.id.logout)

        menuLogout.isVisible = false
        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu

        val item = menu.findItem(R.id.save)
        item.setActionView(R.layout.item_menu_toolbar_simpan)

        val actionView = item.actionView
        val btnSimpan = actionView?.findViewById<TextView>(R.id.textSimpan)
        btnSimpan?.setOnClickListener {
            // your code here
//            saveReimbursement(savedUser)

        }

    }

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
    }
}