package com.example.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.ItemReimbursementBinding
import com.example.dsmabsen.databinding.ItemRekapAbsensiBinding
import com.example.dsmabsen.helper.Helper
import com.example.dsmabsen.model.DataXXXXXXX
import com.example.dsmabsen.model.DataXXXXXXXXXX

class ReimbursementAdapter(private val context: Context) :
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
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<DataXXXXXXX>() {
        override fun areItemsTheSame(oldItem: DataXXXXXXX, newItem: DataXXXXXXX): Boolean {
            return oldItem.created_at == newItem.created_at
        }

        override fun areContentsTheSame(oldItem: DataXXXXXXX, newItem: DataXXXXXXX): Boolean {
            return oldItem.created_at == newItem.created_at
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}