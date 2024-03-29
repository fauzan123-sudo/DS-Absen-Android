package com.infinity.dsmabsen.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.adapter.ReimbursementAdapter
import com.infinity.dsmabsen.databinding.FragmentReimbursementBinding
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.activity.MainActivity
import com.infinity.dsmabsen.ui.viewModel.ReimbursementViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class ReimbursementFragment :
    BaseFragment<FragmentReimbursementBinding>(FragmentReimbursementBinding::inflate) {

    val viewModel: ReimbursementViewModel by viewModels()
    private lateinit var adapter: ReimbursementAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        setupToolbar("Reimbursement")
        val savedUser = Paper.book().read<DataX>("user")
        with(binding) {
            loadingInclude.loading.visibility = View.VISIBLE
            imgNoData.isVisible = false
            viewModel.requestReimbursement(savedUser!!.nip)
            viewModel.getReimbursementLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {

                        loadingInclude.loading.visibility = View.GONE
                        val response = it.data!!
                        val status = response.status
                        if (status) {
                            if (response.data.data.isEmpty()) {
                                recReimbursement.isVisible = false
                                imgNoData.isVisible = true
                            } else {
                                Log.d("semua reimbursement", response.data.data.toString())
                                adapter = ReimbursementAdapter(requireContext(), response.data.data){ reimbursement ->
                                    val action = ReimbursementFragmentDirections.actionReimbursementFragmentToDetailReimbursementFragment(reimbursement)
                                    findNavController().navigate(action)
//                                    val action =
//                                        AktivitasFragmentDirections.actionAktivitasFragmentToDetailAktivitasFragment(
//                                            aktivitas
//                                        )
//                                    findNavController().navigate(action)

                                }
                                recyclerView = recReimbursement
                                recyclerView.adapter = adapter
                                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                                recyclerView.setHasFixedSize(true)
                                recReimbursement.isVisible = true
                                imgNoData.isVisible = false
                            }
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.VISIBLE
                            recReimbursement.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            recReimbursement.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }
                }

            }
        }

        super.onViewCreated(view, savedInstanceState)

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    // Handle add menu item click
                    findNavController().navigate(R.id.action_reimbursementFragment_to_pengajuanReimbursementFragment)
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
        menuLogout.isVisible = false
        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = true // menyembunyikan menu tertentu
    }

//    override fun onConnectionAvailable() {
//        super.onConnectionAvailable()
//        binding.apply {
//            toolbar.toolbar.visibility = View.VISIBLE
//            recReimbursement.visibility = View.VISIBLE
//            noInternetConnection.ivNoConnection.visibility = View.GONE
//        }
//    }
//
//    override fun onConnectionLost() {
//        super.onConnectionLost()
//        binding.apply {
//            toolbar.toolbar.visibility = View.GONE
//            recReimbursement.visibility = View.GONE
//            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
//        }
//    }

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
        showBottomNavigation()
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