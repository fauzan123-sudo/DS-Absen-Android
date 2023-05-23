package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentAllProfileBinding
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.ui.viewModel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class AllProfileFragment :
    BaseFragment<FragmentAllProfileBinding>(FragmentAllProfileBinding::inflate) {

    private val viewModel: UserProfileViewModel by viewModels()
    val savedUser = Paper.book().read<DataX>("user")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigation()
        with(binding) {
            rvDataPribadi.setOnClickListener {
                findNavController()
                    .navigate(R.id.action_allProfileFragment_to_personalDataFragment)
            }

            rvJabatan.setOnClickListener {
                findNavController().navigate(R.id.action_allProfileFragment_to_jabatanDanPosisiFragment)
            }

            rvPendidikan.setOnClickListener {
                findNavController().navigate(R.id.action_allProfileFragment_to_pendidikanFragment)
            }

            rvLokasiKerja.setOnClickListener {
                findNavController().navigate(R.id.action_allProfileFragment_to_lokasiKerjaFragment)
            }

            rvPenguasaanBahasa.setOnClickListener {
                findNavController().navigate(R.id.action_allProfileFragment_to_penguasaanBahasaFragment)
            }

            rvPengalamanKerja.setOnClickListener {
                findNavController().navigate(R.id.action_allProfileFragment_to_pengalamanKerjaFragment)
            }
//            viewModel.detailProfileRequest(savedUser!!.nip)
//            viewModel.profileDetailLivedata.observe(viewLifecycleOwner) {
//                when (it) {
//                    is NetworkResult.Success -> {
//                        val response = it.data!!
//                        if (response.status) {
//                            Log.d("sukses allprofile", "")
//                            val data = response.data
//                            val paper = Paper.book()
//
//                            val dataProfile = Paper.book().read<DataXXXXX>("profileUser")
//                            if (dataProfile == null) {
//                                paper.write("profileUser", data)
//                            }
//
//
//                        }
//////                            Pribadi
////                            val dataPribadi = data.data.pribadi
////                            with(dataPribadi) {
////                                val id = id
////                                val nip = nip
////                                val nik = nik
////                                val name = name
////                                val gelarDepan = gelar_depan
////                                val gelarBelakang = gelar_belakang
////                                val tempatLahir = tempat_lahir
////                                val tanggalLahir = tanggal_lahir
////                                val jenisKelamin = jenis_kelamin
////                                val kodeAgama = kode_agama
////                                val kodeStatus = kode_status
////                                val kodeKawin = kode_kawin
////                                val kodeSuku = kode_suku
////                                val golonganDarah = golongan_darah
////                                val alamat = alamat
////                                val alamatKtp = alamat_ktp
////                                val email = email
////                                val noHp = no_hp
////                                val kordinat = kordinat
////                                val latitude = latitude
////                                val longitude = longitude
////                                val jarak = jarak
////                                val emailVerifiedAt = email_verified_at
////                                val deletedAt = deleted_at
////                                val createdAt = created_at
////                                val updatedAt = updated_at
////                                val images = images
////
//
////
//////                            Posisi
////                            val dataPosisi = data.data.posisi
////                            with(dataPosisi) {
////                                val pNip = nip
////                                val pName = name
////                                val pTempatLahir = tempat_lahir
////                                val pTanggalLahir = tanggal_lahir
////                                val pSkpd = skpd
////                                val pParent = parents
////                                val pJabatan = jabatan
////                                val pImages = images
////                                val pAlamat = alamat
////                                val pNoHp = no_hp
////                                val pTmtJabatan = tmt_jabatan
////                                val pJenisJabatan = jenis_jabatan
////                                val pMasakerja = masa_kerja
////                            }
////
//////                            Lokasi kerja
//////                            Pendidikan
////                            val dataPendidikan = data.data.pendidikan
////                            with(dataPendidikan) {
////                                val created_at =
////                                val deleted_at: String,
////                                val `file`: String,
////                                val gelar_belakang: String,
////                                val gelar_depan: String,
////                                val id: Int,
////                                val is_akhir: Int,
////                                val kode_jurusan: String,
////                                val kode_pendidikan: String,
////                                val nama_sekolah: String,
////                                val nip: String,
////                                val nomor_ijazah: String,
////                                val tanggal_lulus: String,
////                                val updated_at: String
////                            }
//
//
//                        else {
//                            Toast.makeText(requireContext(), "ada masalah ", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//
//                    is NetworkResult.Loading -> {
//
//                    }
//
//                    is NetworkResult.Error -> {
//                        Log.d("all profile", it.message.toString())
//                        handleApiError(it.message)
//                    }
//                }
//            }
        }
        setHasOptionsMenu(true)
        setupToolbar("Profile")
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)
        val menuScan = menu.findItem(R.id.scan)

        menuScan.isVisible = false
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
        Log.d("TAG", "onDestroyView: AllProfile")
//        showBottomNavigation()
    }

    override fun onStart() {
        Log.d("TAG", "onStart: hide AllProfile")
        super.onStart()
        hideBottomNavigation()
    }

}