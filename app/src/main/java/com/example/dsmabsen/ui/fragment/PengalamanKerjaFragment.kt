package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentPengalamanKerjaBinding
import com.example.dsmabsen.model.DataXXXXX
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

        menuLogout.isVisible = false
        menuSave?.isVisible = false
        menuPlus?.isVisible = false
    }
}