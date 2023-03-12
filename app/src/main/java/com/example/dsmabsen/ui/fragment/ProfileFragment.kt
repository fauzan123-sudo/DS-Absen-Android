package com.example.dsmabsen.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentProfileBinding
import com.example.dsmabsen.helper.Constans
import com.example.dsmabsen.helper.TokenManager
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.model.RequestNip
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.activity.LoginActivity
import com.example.dsmabsen.ui.activity.MainActivity
import com.example.dsmabsen.ui.viewModel.AuthViewModel
import com.example.dsmabsen.ui.viewModel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: UserProfileViewModel by viewModels()
    private val viewModels: AuthViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedUser = Paper.book().read<DataX>("user")
        val nipUser = savedUser!!.nip

        with(binding) {
            rvProfileLengkap.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_allProfileFragment)
            }



            llLogout.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("LogOut")
                    .setMessage("Anda Yakin ingin logout")
                //    .setIcon(R.drawable.ic_warning)
                    .setPositiveButton("Ya"){ _, _ ->
                        val nips = RequestNip(
                            nipUser
                        )

                        Toast.makeText(requireContext(), "$nips", Toast.LENGTH_SHORT).show()
                        viewModels.requestLogout(nipUser)
                        viewModels.logOutLiveData.observe(viewLifecycleOwner) {
                            binding.loadingInclude.loading.isVisible = false
                            when (it) {
                                is NetworkResult.Success -> {
                                    val data = it.data!!
                                    binding.loadingInclude.loading.isVisible= false
                                    binding.constraintLayout3.isVisible = true
                                    requireActivity().startActivity(
                                        Intent(
                                            requireActivity(),
                                            LoginActivity::class.java
                                        )
                                    )
                                    Paper.book().delete("user")
                                    tokenManager.deleteToken()
                                }

                                is NetworkResult.Loading -> {
                                    binding.constraintLayout3.isVisible = false
                                    binding.loadingInclude.loading.isVisible = true
                                }

                                is NetworkResult.Error -> {
                                    handleApiError(it.message)
                                    Log.d("TAG", it.message.toString())
                                    binding.loadingInclude.loading.isVisible = false
//                            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    .setNegativeButton("Kembali"){_,_ ->
//                        Toast.makeText(requireContext(), "Kembali", Toast.LENGTH_SHORT).show()
                    }
                    .create().show()

            }
        }

        viewModel.profileUserRequest(savedUser!!.nip)
        viewModel.profileUserLivedata.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    with(binding) {
                        val data = it.data!!
                        if (data.status) {
                            Glide.with(requireContext())
                                .load(Constans.IMAGE_URL + data.data.foto)
                                .into(civProfile)
                        } else {
                            Toast.makeText(requireContext(), "error in here", Toast.LENGTH_SHORT)
                                .show()
                        }

                        tvNamaProfile.text = data.data.nama
                        tvKontrak.text = data.data.status_pegawai
                        tvSurveyor.text = data.data.jabatan
                        tvDivisi.text = data.data.divisi
                    }
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