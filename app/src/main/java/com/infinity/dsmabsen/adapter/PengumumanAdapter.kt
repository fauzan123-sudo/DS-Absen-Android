package com.infinity.dsmabsen.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ItemPengumumanBinding
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

class PengumumanAdapter(
    private val context: Context,
    private val listData: List<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX>,
    private val onItemClick: (DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX) -> Unit
) :
    BaseAdapter<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX, ItemPengumumanBinding>(
        context,
        listData,
        ItemPengumumanBinding::inflate
    ) {
    override fun bind(binding: ItemPengumumanBinding, item: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX) {
        binding.apply {
            with(item) {
                tvJudul.text = judul
                tvCreateAt.text = created_at
                tvKeteranganPengumuman.text = deskripsi

                if (file.isEmpty()) {
                    Glide.with(context)
                        .load(file)
                        .placeholder(R.drawable.progress_animation)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivPengumuman)
                } else {
                    Glide.with(context)
                        .load(file)
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.image_not_found)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivPengumuman)
                }

                root.setOnClickListener {
                    onItemClick(item)
                }

            }
        }
    }


}