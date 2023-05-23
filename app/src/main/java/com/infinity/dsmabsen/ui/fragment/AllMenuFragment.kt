package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentAllMenuBinding
import dagger.hilt.android.AndroidEntryPoint

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

            btnAktivitas.setOnClickListener {
                findNavController().navigate(R.id.aktivitasFragment)
            }

            btnBerita.setOnClickListener {
                findNavController().navigate(R.id.pengumumanFragment)
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)

        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.textLogout)
        val menuScan = menu.findItem(R.id.scan)

        menuScan.isVisible = false
        menuLogout.isVisible = false
        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu

        super.onCreateOptionsMenu(menu, inflater)
    }
//    override fun onConnectionAvailable() {
//        super.onConnectionAvailable()
//        binding.apply {
//            toolbar.toolbar.visibility = View.VISIBLE
//            fb1.visibility = View.VISIBLE
//            fb2.visibility = View.VISIBLE
//            fb3.visibility = View.VISIBLE
//            noInternetConnection.ivNoConnection.visibility = View.GONE
//        }
//    }

//    override fun onConnectionLost() {
//        super.onConnectionLost()
//        binding.apply {
//            toolbar.toolbar.visibility = View.GONE
//            fb1.visibility = View.GONE
//            fb2.visibility = View.GONE
//            fb3.visibility = View.GONE
//            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
//        }
//    }
}