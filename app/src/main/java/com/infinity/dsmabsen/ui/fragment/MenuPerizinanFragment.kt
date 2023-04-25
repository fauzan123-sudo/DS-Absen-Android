package com.infinity.dsmabsen.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.adapter.AttendanceAdapter
import com.infinity.dsmabsen.adapter.PerizinanAdapter
import com.infinity.dsmabsen.databinding.FragmentPerizinanBinding
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.activity.MainActivity
import com.infinity.dsmabsen.ui.viewModel.PerizinanViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class MenuPerizinanFragment :
    BaseFragment<FragmentPerizinanBinding>(FragmentPerizinanBinding::inflate) {

    private val viewModel: PerizinanViewModel by viewModels()
    private lateinit var adapter: PerizinanAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        setupToolbar("Perizinan")
        val savedUser = Paper.book().read<DataX>("user")
        with(binding) {
            loadingInclude.loading.visibility = View.VISIBLE
            imgNoData.isVisible = false

            viewModel.requestPerizinan(savedUser!!.nip)
            viewModel.perizinanLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        loadingInclude.loading.visibility = View.GONE
                        val data = it.data!!
                        val status = data.status
                        if (status) {
                            if (data.data.data.isEmpty()) {
                                recPerizinan.isVisible = false
                                imgNoData.isVisible = true
                            } else {
                                adapter = PerizinanAdapter(requireContext(), data.data.data){

                                }
                                recyclerView = recPerizinan
                                recyclerView.adapter = adapter
                                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                                recyclerView.setHasFixedSize(true)
                                recPerizinan.isVisible = true
                                imgNoData.isVisible = false
                            }
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.VISIBLE
                            recPerizinan.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            recPerizinan.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }
                }
            }
        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    // Handle add menu item click
                    findNavController().navigate(R.id.action_menuPerizinanFragment_to_formPerizinanFragment)
                    true
                }

                else -> false
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
        menuSave?.isVisible = false
        menuPlus?.isVisible = true
        menuLogout?.isVisible = false


    }

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            toolbar.toolbar.visibility = View.VISIBLE
            recPerizinan.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            recPerizinan.visibility = View.GONE
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