package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentEditProfileBinding
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.activity.MainActivity
import com.infinity.dsmabsen.ui.viewModel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {
    val savedUser = Paper.book().read<DataX>("user")
    private val argss by navArgs<EditProfileFragmentArgs>()
    private val viewModel: UserProfileViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()

        binding.apply {
            val argument = argss.txtProfile
            txtEdit.text = argument
            edtProfile.setText(argss.valueProfile)
            if (argument == "tanggal lahir") {
                edtProfile.keyListener = null
                btnUbahTanggal.visibility = View.VISIBLE
                btnUbahTanggal.setOnClickListener {
                    val datePicker = MaterialDatePicker.Builder
                        .datePicker()
                        .setTitleText("")
                        .build()
                    datePicker.show(childFragmentManager, "DatePicker")

                    datePicker.addOnPositiveButtonClickListener {
                        val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                        val date = dateFormatter.format(Date(it))
                        edtProfile.setText(date)
                    }

                    datePicker.addOnNegativeButtonClickListener {

                    }

                    datePicker.addOnCancelListener {

                    }

                }
            } else {
                edtProfile.isEnabled = true
                btnUbahTanggal.visibility = View.GONE
            }
        }

        setHasOptionsMenu(true)
        setupToolbar("Edit Profil")
        view.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.save -> {
                        AlertDialog.Builder(requireContext())
                            .setMessage("Anda yakin ingin mengubah data")
                            //    .setIcon(R.drawable.ic_warning)
                            .setPositiveButton("Ya") { _, _ ->
                                savedEdit()
                            }
                            .setNegativeButton("Kembali") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create().show()
                        true
                    }

                    else -> false
                }
            }
        }

    }

    private fun savedEdit() {
        val nip = savedUser!!.nip
        val key = argss.dataProfile
        val value = binding.edtProfile.text.toString()
        Log.d(
            "savedEdit", "nip = $nip ," +
                    "key = $key ," +
                    "value = $value"
        )
        viewModel.editProfileRequest(
            savedUser!!.nip,
            argss.dataProfile,
            binding.edtProfile.text.toString()
        )
        viewModel.editProfileLivedata.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    binding.loadingInclude.loading.visibility = View.GONE
                    binding.rvEditProfile.visibility = View.VISIBLE
                    val response = it.data!!
                    val status = response.status
                    val message = response.data.message
                    AlertDialog.Builder(requireContext())
                        .setMessage(message)
                        .setPositiveButton("Ya") { dialog, _ ->
                            dialog.dismiss()
                            findNavController().navigate(R.id.action_editProfileFragment_to_personalDataFragment)
                        }
                        .create().show()
                }

                is NetworkResult.Loading -> {
                    binding.loadingInclude.loading.visibility = View.VISIBLE
                    binding.rvEditProfile.visibility = View.GONE
                }

                is NetworkResult.Error -> {
                    binding.loadingInclude.loading.visibility = View.GONE
                    binding.rvEditProfile.visibility = View.VISIBLE
                    handleApiError(it.message)
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
        menuLogout.isVisible = false
        menuSave?.isVisible = true
        menuPlus?.isVisible = false
    }

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            toolbar.toolbar.visibility = View.VISIBLE
            rvEditProfile.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            rvEditProfile.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
    }

    override fun onResume() {
        super.onResume()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
    }

    override fun onStart() {
        super.onStart()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
    }

    override fun onPause() {
        super.onPause()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
    }
}