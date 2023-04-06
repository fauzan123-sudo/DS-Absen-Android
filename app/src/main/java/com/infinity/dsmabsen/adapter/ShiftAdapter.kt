package com.infinity.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ItemShiftBinding
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXX

class ShiftAdapter(private val context: Context) :
    RecyclerView.Adapter<ShiftAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemShiftBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: DataXXXXXXXXXXXXXXXXXXXXXX) {
            binding.apply {
                with(item) {
                    tvKeteranganShift.text = keterangan
                    tvTanggalShift.text = created_at
                    tvShift2.text = shift

                    when (status_api) {
                        "Diajukan" -> {
                            tvApprove.text = status_api
                            tvApprove.setTextColor(ContextCompat.getColor(context, R.color._info))
                        }
                        "Diterima" -> {
                            tvApprove.text = status_api
                            tvApprove.setTextColor(ContextCompat.getColor(context, R.color._success))
                        }
                        "Ditolak" -> {
                            tvApprove.text = status_api
                            tvApprove.setTextColor(ContextCompat.getColor(context, R.color._danger))
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemShiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<DataXXXXXXXXXXXXXXXXXXXXXX>() {
        override fun areItemsTheSame(
            oldItem: DataXXXXXXXXXXXXXXXXXXXXXX,
            newItem: DataXXXXXXXXXXXXXXXXXXXXXX
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DataXXXXXXXXXXXXXXXXXXXXXX,
            newItem: DataXXXXXXXXXXXXXXXXXXXXXX
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}