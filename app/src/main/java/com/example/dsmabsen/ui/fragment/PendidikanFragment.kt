package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.adapter.BahasaAdapter
import com.example.dsmabsen.adapter.PendidikanAdapter
import com.example.dsmabsen.databinding.FragmentPendidikanBinding
import com.example.dsmabsen.databinding.FragmentPengalamanKerjaBinding
import com.example.dsmabsen.model.DataXXXXX
import io.paperdb.Paper

class PendidikanFragment :
    BaseFragment<FragmentPendidikanBinding>(FragmentPendidikanBinding::inflate) {

    private lateinit var adapter: PendidikanAdapter
    private lateinit var recyclerView: RecyclerView
    private val profileUser = Paper.book().read<DataXXXXX>("profileUser")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setHasOptionsMenu(true)
            setupToolbar("Pendidikan")
            adapter = PendidikanAdapter(requireContext())
            recyclerView = recPendidikan
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)

            adapter.differ.submitList(profileUser!!.pendidikan)

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