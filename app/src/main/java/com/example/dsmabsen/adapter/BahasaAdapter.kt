package com.example.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.ItemBahasaBinding
import com.example.dsmabsen.databinding.ItemRekapAbsensiBinding
import com.example.dsmabsen.helper.Helper
import com.example.dsmabsen.model.Bahasa
import com.example.dsmabsen.model.DataXXXXX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXX

class BahasaAdapter(private val context: Context) :
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