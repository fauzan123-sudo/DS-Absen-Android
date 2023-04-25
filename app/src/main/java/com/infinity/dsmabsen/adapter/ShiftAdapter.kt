package com.infinity.dsmabsen.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ItemShiftBinding
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXX

class ShiftAdapter(
    private val context: Context,
    private val listData: List<DataXXXXXXXXXXXXXXXXXXXXXX>,
    private val onItemClick: (DataXXXXXXXXXXXXXXXXXXXXXX) -> Unit
) :
    BaseAdapter<DataXXXXXXXXXXXXXXXXXXXXXX, ItemShiftBinding>(
        context,
        listData,
        ItemShiftBinding::inflate
    ){
    override fun bind(binding: ItemShiftBinding, item: DataXXXXXXXXXXXXXXXXXXXXXX) {
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
                        tvApprove.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color._success
                            )
                        )
                    }
                    "Ditolak" -> {
                        tvApprove.text = status_api
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