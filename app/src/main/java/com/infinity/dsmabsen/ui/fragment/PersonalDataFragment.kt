package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentPersonalDataBinding
import com.infinity.dsmabsen.helper.Helper
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.activity.MainActivity
import com.infinity.dsmabsen.ui.viewModel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class PersonalDataFragment :
    BaseFragment<FragmentPersonalDataBinding>(FragmentPersonalDataBinding::inflate) {
    val savedUser = Paper.book().read<DataX>("user")

    private val viewModel: UserProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myActivities = activity as MainActivity
//        myActivities.hideMyBottomNav()
        hideBottomNavigation()
        loadData()
        binding.swipePersonalData.setOnRefreshListener {
//            Handler(Looper.getMainLooper()).postDelayed({
//                loadDataResfresh()
//            }, 2000)
            loadDataResfresh()
        }
        setHasOptionsMenu(true)
        setupToolbar("Data Pribadi")
        view.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.save -> {
                        // Handle add menu item click
//                        saveReimbursement(savedUser)
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun loadData() {
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        hideBottomNavigation()
        binding.apply {
            viewModel.detailProfileRequest(savedUser!!.nip)
            viewModel.profileDetailLivedata.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        scrollview.visibility = View.VISIBLE
                        loadingInclude.loading.visibility = View.GONE
                        val response = it.data!!

                        val tanggal = response.data.pribadi.tanggal_lahir
                        tvNip.text = response.data.pribadi.nip
                        tvGelarDepan.text = response.data.pribadi.gelar_depan
                        tvNama.text = response.data.pribadi.name
                        tvGelarBelakang.text = response.data.pribadi.gelar_belakang
                        tvTempatLahir.text = response.data.pribadi.tempat_lahir
                        tvTanggalLahir.text = Helper().convertTanggal(tanggal!!, "dd-MM-yyyy")
                        tvJenisKelamin.text = response.data.pribadi.jenis_kelamin
                        tvAgama.text = response.data.pribadi.kode_agama
                        tvStatusPerkawinan.text = response.data.pribadi.kode_kawin
                        tvGolongan.text = response.data.pribadi.golongan_darah
                        tvNoHp.text = response.data.pribadi.no_hp
                        tvNoKtp.text = response.data.pribadi.nik

                        rvGelarDepan.setOnClickListener {
                            val text = "Gelar Depan"
                            val data = "gelar_depan"

                            val value = response.data.pribadi.gelar_depan ?: ""

                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value, text
                                )
                            findNavController().navigate(action)
                        }
                        rvNama.setOnClickListener {
                            val text = "Nama"
                            val data = "name"
                            val value = response.data.pribadi.name ?: ""
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvGelarBelakang.setOnClickListener {
                            val text = "Gelar Belakang"
                            val data = "gelar_belakang"
                            val value = response.data.pribadi.gelar_belakang ?: ""
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvTempatLahir.setOnClickListener {
                            val text = "Tempat Lahir"
                            val data = "tempat_lahir"
                            val value = response.data.pribadi.tempat_lahir ?: ""
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvTanggalLahir.setOnClickListener {
                            val text = "Tanggal Lahir"
                            val data = "tanggal_lahir"
                            val tanggal = response.data.pribadi.tanggal_lahir ?: ""
                            val value = Helper().convertTanggal(tanggal!!, "dd-MM-yyyy")
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvJenisKelamin.setOnClickListener {
                            val text = "Jenis Kelamin"
                            val data = "jenis_kelamin"
                            val value = response.data.pribadi.jenis_kelamin ?: ""
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvAgama.setOnClickListener {
                            val text = "Agama"
                            val data = "kode_agama"
                            val value = response.data.pribadi.kode_agama ?: ""
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvStatusPerkawinan.setOnClickListener {
                            val text = "Status Kawin"
                            val data = "kode_kawin"
                            val value = response.data.pribadi.kode_kawin ?: ""
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvGolongan.setOnClickListener {
                            val text = "Golongan Darah"
                            val data = "golongan_darah"
                            val value = response.data.pribadi.golongan_darah ?: ""
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvNoKtp.setOnClickListener {
                            val text = "NIK"
                            val data = "nik"
                            val value = response.data.pribadi.nik ?: ""
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvNoHp.setOnClickListener {
                            val text = "Nomor HP"
                            val data = "no_hp"
                            val value = response.data.pribadi.no_hp ?: ""
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvEmail.setOnClickListener {
                            val text = "Email"
                            val data = "email"
                            val value = response.data.pribadi.email ?: ""
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                    }

                    is NetworkResult.Loading -> {
                        loadingInclude.loading.visibility = View.VISIBLE
                        scrollview.visibility = View.INVISIBLE
                    }

                    is NetworkResult.Error -> {
                        scrollview.visibility = View.VISIBLE
                        loadingInclude.loading.visibility = View.GONE
                        handleApiError(it.message)
                    }
                }
            }
        }
    }

    private fun loadDataResfresh() {
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        hideBottomNavigation()
        binding.apply {
            viewModel.detailProfileRequest(savedUser!!.nip)
            viewModel.profileDetailLivedata.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        scrollview.visibility = View.VISIBLE
                        swipePersonalData.isRefreshing = false
                        val response = it.data!!

                        tvNip.text = response.data.pribadi.nip
                        tvGelarDepan.text = response.data.pribadi.gelar_depan
                        tvNama.text = response.data.pribadi.name
                        tvGelarBelakang.text = response.data.pribadi.gelar_belakang
                        tvTempatLahir.text = response.data.pribadi.tempat_lahir
                        tvTanggalLahir.text = response.data.pribadi.tanggal_lahir
                        tvJenisKelamin.text = response.data.pribadi.jenis_kelamin
                        tvAgama.text = response.data.pribadi.kode_agama
                        tvStatusPerkawinan.text = response.data.pribadi.kode_kawin
                        tvGolongan.text = response.data.pribadi.golongan_darah
                        tvNoHp.text = response.data.pribadi.no_hp
                        tvNoKtp.text = response.data.pribadi.nik

                        rvGelarDepan.setOnClickListener {
                            val text = "Gelar Depan"
                            val data = "gelar_depan"
                            val value = response.data.pribadi.gelar_depan
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvNama.setOnClickListener {
                            val text = "Nama"
                            val data = "name"
                            val value = response.data.pribadi.name
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvGelarBelakang.setOnClickListener {
                            val text = "Gelar Belakang"
                            val data = "gelar_belakang"
                            val value = response.data.pribadi.gelar_belakang
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvTempatLahir.setOnClickListener {
                            val text = "Tempat Lahir"
                            val data = "tempat_lahir"
                            val value = response.data.pribadi.tempat_lahir
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvTanggalLahir.setOnClickListener {
                            val text = "Tanggal Lahir"
                            val data = "tanggal_lahir"
                            val value = response.data.pribadi.tanggal_lahir
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvJenisKelamin.setOnClickListener {
                            val text = "Jenis Kelamin"
                            val data = "jenis_kelamin"
                            val value = response.data.pribadi.jenis_kelamin
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvAgama.setOnClickListener {
                            val text = "Agama"
                            val data = "kode_agama"
                            val value = response.data.pribadi.kode_agama
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvStatusPerkawinan.setOnClickListener {
                            val text = "Status Kawin"
                            val data = "kode_kawin"
                            val value = response.data.pribadi.kode_kawin
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvGolongan.setOnClickListener {
                            val text = "Golongan Darah"
                            val data = "golongan_darah"
                            val value = response.data.pribadi.golongan_darah
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvNoKtp.setOnClickListener {
                            val text = "NIK"
                            val data = "nik"
                            val value = response.data.pribadi.nik
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvNoHp.setOnClickListener {
                            val text = "Nomor HP"
                            val data = "no_hp"
                            val value = response.data.pribadi.no_hp
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                        rvEmail.setOnClickListener {
                            val text = "Email"
                            val data = "email"
                            val value = response.data.pribadi.email
                            val action =
                                PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
                                    data, value!!, text
                                )
                            findNavController().navigate(action)
                        }
                    }

                    is NetworkResult.Loading -> {
                        swipePersonalData.isRefreshing = true
                        scrollview.visibility = View.INVISIBLE
                    }

                    is NetworkResult.Error -> {
                        scrollview.visibility = View.VISIBLE
                        swipePersonalData.isRefreshing = false
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
        menuLogout.isVisible = false
        menuSave?.isVisible = false // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu

    }

//    override fun onConnectionAvailable() {
//        super.onConnectionAvailable()
//    }
//
//    override fun onConnectionLost() {
//        super.onConnectionLost()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
//        showBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onDestroyView: hide PersonalData")
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        hideBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onDestroyView: hide PersonalData")
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        hideBottomNavigation()
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "onDestroyView: hide PersonalData")
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        hideBottomNavigation()
    }
}