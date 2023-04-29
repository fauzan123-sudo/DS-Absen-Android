package com.infinity.dsmabsen.ui.fragment

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentProfileBinding
import com.infinity.dsmabsen.databinding.LayoutWarningDailogBinding
import com.infinity.dsmabsen.helper.TokenManager
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.model.RequestNip
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.activity.LoginActivity
import com.infinity.dsmabsen.ui.viewModel.AuthViewModel
import com.infinity.dsmabsen.ui.viewModel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: UserProfileViewModel by viewModels()
    private val viewModels: AuthViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager
    val savedUser = Paper.book().read<DataX>("user")
    private val nipUser = savedUser!!.nip

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setHasOptionsMenu(true)

            setupToolbar("Profile")
            rvProfileLengkap.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_allProfileFragment)
            }
            logout.setOnClickListener {
//                logout(nipUser)
                loginOut()
            }
        }

        viewModel.profileUserRequest(savedUser!!.nip)
        viewModel.profileUserLivedata.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    with(binding) {
                        val data = it.data!!
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            constraintLayout3.visibility = View.VISIBLE
                        }
                        if (data.status) {
                            Glide.with(requireContext())
                                .load(data.data.foto)
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
                    binding.apply {
                        loadingInclude.loading.visibility = View.VISIBLE
                        constraintLayout3.visibility = View.GONE
                    }
                }

                is NetworkResult.Error -> {
                    binding.apply {
                        loadingInclude.loading.visibility = View.GONE
                        constraintLayout3.visibility = View.VISIBLE
                    }
                    handleApiError(it.message)
                }
            }
        }
    }

//    ws lek jajal sue njirr wes lek
//    iki fungsi logout ng ak kurang fokus wkwkwk


    private fun loginOut() {
        Log.d("btn_logout", "loginOut: ")
        val dialogBinding = LayoutWarningDailogBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.textTitle.text = "Konfirmasi Keluar"
        dialogBinding.textMessage.text = "Anda yakin Ingin keluar dari this aplikasi"
        dialogBinding.buttonYes.text = "Ya"
        dialogBinding.buttonNo.text = "Batal"
        dialogBinding.imageIcon.setImageResource(R.drawable.ic_baseline_warning_24)

        dialogBinding.buttonYes.setOnClickListener {
            alertDialog.dismiss()
            viewModels.requestLogout(nipUser)
            viewModels.logOutLiveData.observe(viewLifecycleOwner) {
                binding.loadingInclude.loading.visibility = View.GONE
                when (it) {
                    is NetworkResult.Success -> {
                        val data = it.data!!
                        binding.loadingInclude.loading.visibility = View.GONE
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
                        binding.loadingInclude.loading.visibility = View.GONE
                    }

                    is NetworkResult.Error -> {
                        handleApiError(it.message)
                        Log.d("TAG", it.message.toString())
                        binding.loadingInclude.loading.visibility = View.GONE
                        //                            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        dialogBinding.buttonNo.setOnClickListener {
            alertDialog.dismiss()
        }

        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        alertDialog.show()
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

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            constraintLayout3.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            constraintLayout3.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }
}