package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
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

}