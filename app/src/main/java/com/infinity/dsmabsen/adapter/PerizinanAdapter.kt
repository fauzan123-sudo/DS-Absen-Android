package com.infinity.dsmabsen.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ItemPerizinanBinding
import com.infinity.dsmabsen.databinding.ItemRekapAbsensiBinding
import com.infinity.dsmabsen.helper.Helper
import com.infinity.dsmabsen.model.DataXXXXXXXXXX
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXX

class PerizinanAdapter(private val context: Context, private val listData: List<DataXXXXXXXXXXXX>) :
    RecyclerView.Adapter<PerizinanAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPerizinanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(item: DataXXXXXXXXXXXX) {
            binding.apply {
                with(item) {
                    tanggal.text = created_at
                    durasi.text = "$tanggal_mulai - $tanggal_selesai"
                    keteranganUser.text = keterangan

                    when (status) {
                        "Diajukan" -> {
                            tvApprove.text = status
                            tvApprove.setTextColor(ContextCompat.getColor(context, R.color._info))
                        }
                        "Diterima" -> {
                            tvApprove.text = status
                            tvApprove.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color._success
                                )
                            )
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
        ItemPerizinanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(listData[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = listData.size

}