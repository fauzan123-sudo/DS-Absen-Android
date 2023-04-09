package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.adapter.BahasaAdapter
import com.infinity.dsmabsen.databinding.FragmentPenguasaanBahasaBinding
import com.infinity.dsmabsen.model.DataXXXXX
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
        val menuScan = menu.findItem(R.id.scan)

        menuScan?.isVisible = false
        menuLogout.isVisible = false
        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu
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