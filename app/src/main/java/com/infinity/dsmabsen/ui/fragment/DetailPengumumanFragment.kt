package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentDetailPengumumanBinding

class DetailPengumumanFragment :
    BasesFragment<FragmentDetailPengumumanBinding>(FragmentDetailPengumumanBinding::inflate) {

    private val args: DetailPengumumanFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = args.detailPengumuman

        with(binding) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar.toolbarImage)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.toolbarImage.title = "Detail Visit"
            toolbar.toolbarImageView.visibility = View.INVISIBLE
            Glide.with(requireContext())
                .load(data.file)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.image_not_found)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgPengumuman)
            tvTitle.text = data.judul
            tvContent.text = data.deskripsi
        }
    }
}