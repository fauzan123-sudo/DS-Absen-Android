package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
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
            val value = argss.valueProfile
            Log.d("nilai", value)
            edtProfile.setText(argss.valueProfile)
            edtProfile.hint = argument
            when (argument) {
                "Tanggal Lahir" -> {
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
                }
                "Golongan Darah" -> {
                    edtProfile.keyListener = null
                    edtProfile.visibility = View.INVISIBLE
                    spinnerGolonganDarah.visibility = View.VISIBLE
                    val bloodTypes = resources.getStringArray(R.array.blood_types)
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        bloodTypes
                    )
                    spinnerGolonganDarah.adapter = adapter

                    if (value.isNotEmpty()) {
                        val position = bloodTypes.indexOf(value)
                        Log.d("posisi", position.toString())
                        if (position >= 0) {
                            val getSpinner = spinnerGolonganDarah.setSelection(position)
                            val getSpinners = spinnerGolonganDarah.selectedItem.toString()
//                            Log.d("spinner", getSpinner.toString())
                            Log.d("spinners", getSpinners)
                        }
                    }

                    spinnerGolonganDarah.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val selectedItem = parent.getItemAtPosition(position).toString()
                                Log.d("EditProfileFragment", "Selected blood type: $selectedItem")
                                // Set the selected value to the text of edtProfile
                                edtProfile.setText(selectedItem)

                                // Set the keyListener back to normal
                                edtProfile.keyListener =
                                    DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // Do nothing
                            }
                        }
                }
                "Status Kawin" -> {
                    edtProfile.keyListener = null
                    edtProfile.visibility = View.INVISIBLE
                    spinnerGolonganDarah.visibility = View.VISIBLE
                    val bloodTypes = resources.getStringArray(R.array.status_kawin)
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        bloodTypes
                    )
                    spinnerGolonganDarah.adapter = adapter
                    if (value.isNotEmpty()) {
                        val position = bloodTypes.indexOf(value)
                        if (position >= 0) {
                            val getSpinner = spinnerGolonganDarah.setSelection(position)
                            val getSpinners = spinnerGolonganDarah.selectedItem.toString()
//                            Log.d("spinner", getSpinner.toString())
                            Log.d("spinners", getSpinners)
                        }
                    }

                    spinnerGolonganDarah.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val selectedItem = parent.getItemAtPosition(position).toString()
                                Log.d("status kawin", selectedItem)
                                // Set the selected value to the text of edtProfile
                                edtProfile.setText(selectedItem)

                                // Set the keyListener back to normal
                                edtProfile.keyListener =
                                    DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // Do nothing
                            }
                        }
                }
                "Agama" -> {
                    edtProfile.keyListener = null
                    edtProfile.visibility = View.INVISIBLE
                    spinnerGolonganDarah.visibility = View.VISIBLE
                    val bloodTypes = resources.getStringArray(R.array.jenis_agama)
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        bloodTypes
                    )
                    spinnerGolonganDarah.adapter = adapter
//                    Log.d("data", value)
                    if (value.isNotEmpty()) {
                        val position = bloodTypes.indexOf(value)
                        if (position >= 0) {
                            val getSpinner = spinnerGolonganDarah.setSelection(position)
                            val getSpinners = spinnerGolonganDarah.selectedItem.toString()
//                            Log.d("spinner", getSpinner.toString())

                        }
                    }

                    spinnerGolonganDarah.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val selectedItem = parent.getItemAtPosition(position).toString()
//                                Log.d("agama", selectedItem)
                                // Set the selected value to the text of edtProfile
                                edtProfile.setText(selectedItem)

                                // Set the keyListener back to normal
                                edtProfile.keyListener =
                                    DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // Do nothing
                            }
                        }
                }

                "Jenis Kelamin" -> {
                    edtProfile.keyListener = null
                    edtProfile.visibility = View.INVISIBLE
                    spinnerGolonganDarah.visibility = View.VISIBLE
                    val bloodTypes = resources.getStringArray(R.array.jenis_kelamin)
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        bloodTypes
                    )
                    spinnerGolonganDarah.adapter = adapter
                    if (value.isNotEmpty()) {
                        val defaultValue = toPascalCase(value)
                        Log.d("kelamin", defaultValue)
                        val position = bloodTypes.indexOf(defaultValue)
                        if (position >= 0) {
                            val getSpinner = spinnerGolonganDarah.setSelection(position)
                            val getSpinners = spinnerGolonganDarah.selectedItem.toString()
//                            Log.d("spinner", getSpinner.toString())
                            Log.d("spinners", getSpinners)
                        }
                    }

                    spinnerGolonganDarah.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val selectedItem = parent.getItemAtPosition(position).toString()
//                                Log.d("Jenis Kelamin", selectedItem)
                                // Set the selected value to the text of edtProfile
                                edtProfile.setText(selectedItem)

                                // Set the keyListener back to normal
                                edtProfile.keyListener =
                                    DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // Do nothing
                            }
                        }
                }
                else -> {
                    edtProfile.isEnabled = true
                    btnUbahTanggal.visibility = View.GONE
                }
            }
        }

        setHasOptionsMenu(true)
        setupToolbar("Edit Profil")
        view.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.save -> {
                        if (binding.edtProfile.text.toString().isEmpty()) {
                            binding.edtProfile.error = "harap isi bidang"
                            binding.edtProfile.requestFocus()
                        } else {
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
                        }

                        true
                    }

                    else -> false
                }
            }
        }

    }

    private fun toPascalCase(input: String): String {
        var result = ""
        when (input) {
            "laki-laki" -> result = "Laki-Laki"
            "Laki-laki" -> result = "Laki-Laki"
            "perempuan" -> result = "Perempuan"
        }

        return result
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