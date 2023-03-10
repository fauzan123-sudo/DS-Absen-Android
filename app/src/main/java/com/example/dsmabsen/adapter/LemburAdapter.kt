package com.example.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.ItemLemburBinding
import com.example.dsmabsen.databinding.ItemRekapAbsensiBinding
import com.example.dsmabsen.helper.Helper
import com.example.dsmabsen.model.DataXXXXXXXXXX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXX

class LemburAdapter(private val context: Context) :
    RecyclerView.Adapter<LemburAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLemburBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: DataXXXXXXXXXXXXXXXXX) {
            binding.apply {
                with(item) {
                    tvTanggalLembur.text = created_at
                    jamLembur.text = "$jam_mulai - $jam_selesai"
                    tanggalLembur.text = tanggal
                    tvKeteranganLembur.text = keterangan

                    when (status) {
                        "Diajukan" -> {
                            tvApprove.text = status
                            tvApprove.setTextColor(ContextCompat.getColor(context, R.color._info))
                        }
                        "Diterima" -> {
                            tvApprove.text = status
                            tvApprove.setTextColor(ContextCompat.getColor(context, R.color._success))
                        }
                        "Ditolak" -> {
                            tvApprove.text = status
                            tvApprove.setTextColor(ContextCompat.getColor(context, R.color._danger))
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemLemburBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<DataXXXXXXXXXXXXXXXXX>() {
        override fun areItemsTheSame(oldItem: DataXXXXXXXXXXXXXXXXX, newItem: DataXXXXXXXXXXXXXXXXX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataXXXXXXXXXXXXXXXXX, newItem: DataXXXXXXXXXXXXXXXXX): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}