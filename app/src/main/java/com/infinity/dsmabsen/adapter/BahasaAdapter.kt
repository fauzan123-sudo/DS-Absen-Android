package com.infinity.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.databinding.ItemBahasaBinding
import com.infinity.dsmabsen.model.Bahasa

class BahasaAdapter(private val context: Context ) :
    RecyclerView.Adapter<BahasaAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemBahasaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: Bahasa) {
            binding.apply {
                with(item) {
                    tvNamaBahasa.text = nama_bahasa
                    tvJenis.text = jenis
                    tvPenguasaan.text = penguasaan
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemBahasaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<Bahasa>() {
        override fun areItemsTheSame(oldItem: Bahasa, newItem: Bahasa): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Bahasa, newItem: Bahasa): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}