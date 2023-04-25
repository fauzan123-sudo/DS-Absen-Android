package com.infinity.dsmabsen.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.adapter.LemburAdapter
import com.infinity.dsmabsen.adapter.SallaryAdapter
import com.infinity.dsmabsen.databinding.FragmentLemburBinding
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.activity.MainActivity
import com.infinity.dsmabsen.ui.viewModel.AttendanceViewModel
import com.infinity.dsmabsen.ui.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class LemburFragment : BaseFragment<FragmentLemburBinding>(FragmentLemburBinding::inflate) {

    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var adapter: LemburAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        setHasOptionsMenu(true)
        setupToolbar("Lembur")
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    findNavController().navigate(R.id.action_lemburFragment_to_pengajuanLemburFragment)

                    true
                }

                else -> false
            }
        }
        with(binding) {
            loadingInclude.loading.visibility = View.VISIBLE
            imgNoData.isVisible = false

            val savedUser = Paper.book().read<DataX>("user")

            viewModel.requestListLembur(savedUser!!.nip)
            viewModel.getListLemburLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val response = it.data!!
                        val status = response.status
                        loadingInclude.loading.visibility = View.GONE
                        if (status) {
                            if(response.data.data.isEmpty()){
                                reclembur.isVisible = false
                                imgNoData.isVisible = true
                            }else{
                                adapter = LemburAdapter(requireContext(), response.data.data){ lembur ->
                                    val action = LemburFragmentDirections.actionLemburFragmentToDetailLemburFragment(lembur)
                                    findNavController().navigate(action)
                                }
                                recyclerView = reclembur
                                recyclerView.adapter = adapter
                                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                                recyclerView.setHasFixedSize(true)
                                reclembur.isVisible = true
                                imgNoData.isVisible = false
                            }
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            binding.loadingInclude.loading.visibility = View.VISIBLE
                            reclembur.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            binding.loadingInclude.loading.visibility = View.GONE
                            reclembur.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }
                }
            }

        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)
        val menuScan = menu.findItem(R.id.scan)

        menuScan?.isVisible = false
        menuLogout?.isVisible = false // menyembunyikan menu tertentu
        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = true // menyembunyikan menu tertentu
    }

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            toolbar.toolbar.visibility = View.VISIBLE
            reclembur.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            reclembur.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        Log.d("TAG", "onResume: ")
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        super.onResume()
    }

    override fun onStart() {
        Log.d("TAG", "onStart: ")
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        super.onStart()
    }

    override fun onPause() {
        Log.d("TAG", "onPause: ")
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        super.onPause()
    }

    override fun onStop() {
        Log.d("TAG", "onStop: ")
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("TAG", "onDestroyView: ")
//        showBottomNavigation()
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("TAG", "onDetach: ")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TAG", "onAttach: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        hideBottomNavigation()
        Log.d("TAG", "onCreate: ")
    }

}