package com.infinity.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ItemLemburBinding
import com.infinity.dsmabsen.databinding.ItemRekapAbsensiBinding
import com.infinity.dsmabsen.helper.Helper
import com.infinity.dsmabsen.model.DataXXXXXXXXXX
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXX
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXX

class LemburAdapter(
    private val context: Context, private val dataList: List<DataXXXXXXXXXXXXXXXXX>,
    private val onItemClick: (DataXXXXXXXXXXXXXXXXX) -> Unit
) : BaseAdapter<DataXXXXXXXXXXXXXXXXX, ItemLemburBinding>(
    context,
    dataList,
    ItemLemburBinding::inflate
) {
    override fun bind(binding: ItemLemburBinding, item: DataXXXXXXXXXXXXXXXXX) {
        with(binding) {
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

                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }
}