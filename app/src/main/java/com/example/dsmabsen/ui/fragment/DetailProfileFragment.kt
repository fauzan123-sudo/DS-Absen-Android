package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentDetailProfileBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class DetailProfileFragment : BaseFragment<FragmentDetailProfileBinding>(FragmentDetailProfileBinding::inflate){

    private val viewModel: UserProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val savedUser = Paper.book().read<DataX>("user")
        setHasOptionsMenu(true)
        setupToolbar("Detail Profil")

        with(binding) {
            viewModel.detailProfileRequest(savedUser!!.nip)
            viewModel.profileDetailLivedata.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {

                    }

                    is NetworkResult.Loading -> {

                    }

                    is NetworkResult.Error -> {
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

        menuLogout.isVisible = false
        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu


    }
    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            toolbar.toolbar.visibility = View.VISIBLE
            listdetail.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            listdetail.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }
}