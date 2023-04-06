package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentJabatanDanPosisiBinding
import com.infinity.dsmabsen.helper.TokenManager
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.model.DataXXXXX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.viewModel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import javax.inject.Inject

@AndroidEntryPoint
class JabatanDanPosisiFragment :
    BaseFragment<FragmentJabatanDanPosisiBinding>(FragmentJabatanDanPosisiBinding::inflate) {
    val savedUser = Paper.book().read<DataX>("user")

    @Inject
    lateinit var tokenManager: TokenManager
    private val viewModel: UserProfileViewModel by viewModels()
    private val profileUser = Paper.book().read<DataXXXXX>("profileUser")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setupToolbar("Jabatan dan Posisi")

        viewModel.detailProfileRequest(savedUser!!.nip)
        viewModel.profileDetailLivedata.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val response = it.data!!
                    val jabPos = response.data.posisi
                    with(binding) {
                        with(jabPos) {
                            tvNip.text = nip
                            tvNama.text = name
                            tvTempatLahir.text = tempat_lahir
                            tvTanggalLahir.text = tanggal_lahir
                            tvSkpd.text = skpd
                            tvJabatan.text = jabatan
                            tvAlamat.text = alamat
                            tvNoHp.text = no_hp
                            tvTmtJabatan.text = tmt_jabatan
                            tvJenisJabatan.text = jabatan
                            tvMasaKerja.text = masa_kerja

//                            rvJabatan.setOnClickListener {
//                                val data = "gelar_depan"
//                                val value = response.data.pribadi.gelar_depan
//                                val action =
//                                    PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
//                                        data, value!!
//                                    )
//                                findNavController().navigate(action)
//                            }
//                            rvNama.setOnClickListener {
//                                val data = "name"
//                                val value = response.data.pribadi.name
//                                val action =
//                                    PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
//                                        data, value!!
//                                    )
//                                findNavController().navigate(action)
//                            }
//                            rvGelarBelakang.setOnClickListener {
//                                val data = "gelar_belakang"
//                                val value = response.data.pribadi.gelar_belakang
//                                val action =
//                                    PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
//                                        data
//                                    )
//                                findNavController().navigate(action)
//                            }
//                            rvTempatLahir.setOnClickListener {
//                                val data = "tempat_lahir"
//                                val value = response.data.pribadi.tempat_lahir
//                                val action =
//                                    PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
//                                        data, value!!
//                                    )
//                                findNavController().navigate(action)
//                            }
//                            rvTanggalLahir.setOnClickListener {
//                                val data = "tanggal_lahir"
//                                val value = response.data.pribadi.tanggal_lahir
//                                val action =
//                                    PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
//                                        data, value!!
//                                    )
//                                findNavController().navigate(action)
//                            }
//                            rvJenisKelamin.setOnClickListener {
//                                val data = "jenis_kelamin"
//                                val value = response.data.pribadi.jenis_kelamin
//                                val action =
//                                    PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
//                                        data, value!!
//                                    )
//                                findNavController().navigate(action)
//                            }
//                            rvAgama.setOnClickListener {
//                                val data = "kode_agama"
//                                val value = response.data.pribadi.kode_agama
//                                val action =
//                                    PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
//                                        data, value!!
//                                    )
//                                findNavController().navigate(action)
//                            }
//                            rvStatusPerkawinan.setOnClickListener {
//                                val data = "kode_kawin"
//                                val value = response.data.pribadi.kode_kawin
//                                val action =
//                                    PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
//                                        data, value!!
//                                    )
//                                findNavController().navigate(action)
//                            }
//                            rvGolongan.setOnClickListener {
//                                val data = "golongan_darah"
//                                val value = response.data.pribadi.golongan_darah
//                                val action =
//                                    PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
//                                        data, value!!
//                                    )
//                                findNavController().navigate(action)
//                            }
//                            rvNoKtp.setOnClickListener {
//                                val data = "nik"
//                                val value = response.data.pribadi.nik
//                                val action =
//                                    PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
//                                        data, value!!
//                                    )
//                                findNavController().navigate(action)
//                            }
//                            rvNoHp.setOnClickListener {
//                                val data = "no_hp"
//                                val value = response.data.pribadi.no_hp
//                                val action =
//                                    PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
//                                        data, value!!
//                                    )
//                                findNavController().navigate(action)
//                            }
//                            rvEmail.setOnClickListener {
//                                val data = "email"
//                                val value = response.data.pribadi.email
//                                val action =
//                                    PersonalDataFragmentDirections.actionPersonalDataFragmentToEditProfileFragment(
//                                        data, value!!
//                                    )
//                                findNavController().navigate(action)
//                            }
                        }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)
        val menuScan = menu.findItem(R.id.scan)

        menuScan?.isVisible = false
        menuLogout.isVisible = false
        menuSave?.isVisible = false
        menuPlus?.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigation()
    }

    override fun onPause() {
        super.onPause()
        hideBottomNavigation()
    }
}