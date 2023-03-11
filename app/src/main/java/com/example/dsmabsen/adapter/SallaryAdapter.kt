package com.example.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.databinding.ItemSallaryBinding
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXX

class SallaryAdapter(private val context: Context) :
    RecyclerView.Adapter<SallaryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSallaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: DataXXXXXXXXXXXXXXX) {
            binding.apply {
                with(item) {
                    tanggals.text = tanggal
                    sallary.text = total
                    idGaji.text = kode_payroll
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemSallaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<DataXXXXXXXXXXXXXXX>() {
        override fun areItemsTheSame(oldItem: DataXXXXXXXXXXXXXXX, newItem: DataXXXXXXXXXXXXXXX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataXXXXXXXXXXXXXXX, newItem: DataXXXXXXXXXXXXXXX): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}