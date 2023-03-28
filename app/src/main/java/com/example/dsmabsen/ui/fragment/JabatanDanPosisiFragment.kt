package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentJabatanDanPosisiBinding
import com.example.dsmabsen.model.DataXXXXX
import io.paperdb.Paper

class JabatanDanPosisiFragment :
    BaseFragment<FragmentJabatanDanPosisiBinding>(FragmentJabatanDanPosisiBinding::inflate) {

    private val profileUser = Paper.book().read<DataXXXXX>("profileUser")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setupToolbar("Jabatan dan Posisi")

        val job = profileUser!!.posisi
        with(binding) {
            with(job) {
                tvNip.text = nip
                tvNama.text = name
                tvTempatLahir.text = tempat_lahir
                tvTanggalLahir.text = tanggal_lahir
                tvSkpd.text = skpd
                tvParents.text = parents
                tvJabatan.text = jabatan
                tvAlamat.text = alamat
                tvNoHp.text = no_hp
                tvTmtJabatan.text = tmt_jabatan
                tvJenisJabatan.text = jabatan
                tvMasaKerja.text = masa_kerja
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)

        menuLogout.isVisible = false
        menuSave?.isVisible = false
        menuPlus?.isVisible = false
    }
}