package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentAllMenuBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AllMenuFragment :BaseFragment<FragmentAllMenuBinding>(FragmentAllMenuBinding::inflate){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar("Semua Menu")
        binding.btnReimbursement.setOnClickListener {
            findNavController().navigate(R.id.action_allMenuFragment_to_reimbursementFragment)
        }
        with(binding) {

            btnPerizinan.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_menuPerizinanFragment)
            }

            btnLembur.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_lemburFragment)
            }

            btnShift.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_shiftFragment)
            }

            btnSalary.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_sallaryFragment)
            }

            btnReimbursement.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_reimbursementFragment)
            }


        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)

        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.textLogout)

        menuLogout.isVisible = false
        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu

        super.onCreateOptionsMenu(menu, inflater)
    }
}