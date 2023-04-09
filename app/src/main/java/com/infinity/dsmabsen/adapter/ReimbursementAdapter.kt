package com.infinity.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ItemReimbursementBinding
import com.infinity.dsmabsen.model.DataXXXXXXX

class ReimbursementAdapter(private val context: Context, private val listData:List<DataXXXXXXX>) :
    RecyclerView.Adapter<ReimbursementAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemReimbursementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: DataXXXXXXX) {
            binding.apply {
                with(item) {
                    tanggalRembursement.text = created_at
                    akomodasi.text = reimbursement
                    sallary.text = nilai

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
        ItemReimbursementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(listData[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = listData.size

}