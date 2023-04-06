package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentPengalamanKerjaBinding
import com.infinity.dsmabsen.model.DataXXXXX
import io.paperdb.Paper

class PengalamanKerjaFragment :
    BaseFragment<FragmentPengalamanKerjaBinding>(FragmentPengalamanKerjaBinding::inflate) {
    private val profileUser = Paper.book().read<DataXXXXX>("profileUser")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupToolbar("Pengalaman Kerja")
        with(binding) {
            tvNip.text = profileUser!!.pribadi.nip
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)
        val menuScan = menu.findItem(R.id.scan)

        menuScan?.isVisible = false
        menuLogout.isVisible = false
        menuSave?.isVisible = false
        menuPlus?.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigation()
    }

    override fun onPause() {
        super.onPause()
        hideBottomNavigation()
    }
}