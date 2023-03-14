package com.example.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.ItemPengumumanBinding
import com.example.dsmabsen.databinding.ItemRekapAbsensiBinding
import com.example.dsmabsen.helper.Constans
import com.example.dsmabsen.helper.Helper
import com.example.dsmabsen.model.DataXXXXXXXXXX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

class PengumumanAdapter(private val context: Context) :
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
                            .into(ivPengumuman)
                    } else {
                        Glide.with(context)
                            .load(file)
                            .placeholder(R.drawable.progress_animation)
                            .error(R.drawable.image_not_found)
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
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback =
        object : DiffUtil.ItemCallback<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX>() {
            override fun areItemsTheSame(
                oldItem: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX,
                newItem: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX,
                newItem: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }

    val differ = AsyncListDiffer(this, differCallback)

}