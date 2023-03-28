package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.BahasaAdapter
import com.example.dsmabsen.databinding.FragmentPenguasaanBahasaBinding
import com.example.dsmabsen.model.DataXXXXX
import io.paperdb.Paper

class PenguasaanBahasaFragment :
    BaseFragment<FragmentPenguasaanBahasaBinding>(FragmentPenguasaanBahasaBinding::inflate) {
    private lateinit var adapter: BahasaAdapter
    private lateinit var recyclerView: RecyclerView
    private val profileUser = Paper.book().read<DataXXXXX>("profileUser")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            adapter = BahasaAdapter(requireContext())
            recyclerView = recBahasa
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)

            adapter.differ.submitList(profileUser!!.bahasa)

            setHasOptionsMenu(true)

            setupToolbar("Penguasaan")
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
    }
}