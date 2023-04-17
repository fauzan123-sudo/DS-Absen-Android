package com.infinity.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ItemPengumumanBinding
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

class PengumumanAdapter(
    private val context: Context,
    private val listData: List<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX>
) :
    RecyclerView.Adapter<PengumumanAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPengumumanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX) {
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
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemPengumumanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(listData[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = listData.size


}