package com.example.dsmabsen.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
                val nips = RequestNip(
                    nipUser
                )

                Toast.makeText(requireContext(), "$nips", Toast.LENGTH_SHORT).show()
                viewModels.requestlogout(nipUser)
                viewModels.logOutLiveData.observe(viewLifecycleOwner) {
                    when (it) {
                        is NetworkResult.Success -> {
                            val data = it.data!!
                            if (data.status) {
                                requireActivity().startActivity(
                                    Intent(
                                        requireActivity(),
                                        LoginActivity::class.java
                                    )
                                )
                                Paper.book().delete("user")
                                tokenManager.deleteToken()
                            }else{
                                Toast.makeText(requireContext(), "Gagal Logout", Toast.LENGTH_SHORT).show()
                            }
                        }

                        is NetworkResult.Loading -> {
                            Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                        }

                        is NetworkResult.Error -> {
                            handleApiError(it.message)
                            Log.d("TAG", it.message.toString())
//                            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
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