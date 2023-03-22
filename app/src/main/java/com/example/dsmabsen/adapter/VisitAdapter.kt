package com.example.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.ItemRekapAbsensiBinding
import com.example.dsmabsen.databinding.ItemVisitBinding
import com.example.dsmabsen.helper.Helper
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

class VisitAdapter(private val context: Context) :
    RecyclerView.Adapter<VisitAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemVisitBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX) {
            binding.apply {
                with(item) {
                    val newFormat = "kk:mm:ss"
                    tvTanggalVisit.text = tanggal
                    tvJamVisit.text = jam
                    tvKeteranganVisit.text = visit
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemVisitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX>() {
        override fun areItemsTheSame(oldItem: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX, newItem: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX, newItem: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}