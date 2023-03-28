package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentPersonalDataBinding
import com.example.dsmabsen.helper.handleApiError
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.model.DataXXXXX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class PersonalDataFragment :
    BaseFragment<FragmentPersonalDataBinding>(FragmentPersonalDataBinding::inflate) {

    private val viewModel: UserProfileViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedUser = Paper.book().read<DataX>("user")
        val profileUser = Paper.book().read<DataXXXXX>("profileUser")
        val pribadi = profileUser!!.pribadi
        with(binding) {
            with(pribadi) {
                tvNip.text = nip
                tvGelarDepan.text = gelar_depan
                tvNama.text = name
                tvGelarBelakang.text = gelar_belakang
                tvTempatLahir.text = tempat_lahir
                tvTanggalLahir.text = tanggal_lahir
                tvJenisKelamin.text = jenis_kelamin
                tvAgama.text = kode_agama
                tvStatusPerkawinan.text = kode_kawin
                tvGolongan.text = golongan_darah

            }

            viewModel.detailProfileRequest(savedUser!!.nip)
            viewModel.profileDetailLivedata.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val data = it.data!!
                        if (data.status) {
//                            Pribadi
                            val dataPribadi = data.data.pribadi
                            with(dataPribadi) {
                                val id = id
                                val nip = nip
                                val nik = nik
                                val name = name
                                val gelarDepan = gelar_depan
                                val gelarBelakang = gelar_belakang
                                val tempatLahir = tempat_lahir
                                val tanggalLahir = tanggal_lahir
                                val jenisKelamin = jenis_kelamin
                                val kodeAgama = kode_agama
                                val kodeStatus = kode_status
                                val kodeKawin = kode_kawin
                                val kodeSuku  =  kode_suku
                                val golonganDarah = golongan_darah
                                val alamat = alamat
                                val alamatKtp = alamat_ktp
                                val email = email
                                val noHp = no_hp
                                val kordinat = kordinat
                                val latitude = latitude
                                val longitude = longitude
                                val jarak = jarak
                                val emailVerifiedAt = email_verified_at
                                val deletedAt = deleted_at
                                val createdAt = created_at
                                val updatedAt = updated_at
                                val images = images
                            }

//                            Posisi

//                            nip.text =
//                            nip.text = data.data.pribadi.nip
//                            tvNama.text = data.data.name
//                            tvTempatLahir.text = data.data.tempat_lahir
//                            tvTanggalLahir.text = data.data.tanggal_lahir
//                            tvJenisKelamin.text = data.data.jenis_kelamin
//                            tvAgama.text = data.data.kode_agama
//                            tvStatusPerkawinan.text = data.data.kode_kawin
//                            tvGolongan.text = data.data.golongan_darah
//                            tvNoKtp.text = data.data.nik
//                            tvNoHp.text = data.data.no_hp
//                            tvEmail.text = data.data.email
//                            tvGelarDepan.text = data.data.gelar_depan
//                            tvGelarBelakang.text = data.data.gelar_belakang

//                            Posisi

//                            Lokasi Kerja

//

                        } else {
                            Toast.makeText(requireContext(), "ada masalah ", Toast.LENGTH_SHORT)
                                .show()
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
            scrollview.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            scrollview.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }
}