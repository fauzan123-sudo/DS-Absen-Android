package com.infinity.dsmabsen.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ItemPerizinanBinding
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXX

class PerizinanAdapter(
    private val context: Context, private val listData: List<DataXXXXXXXXXXXX>,
    private val onItemClick: (DataXXXXXXXXXXXX) -> Unit
) :
        BaseAdapter<DataXXXXXXXXXXXX,ItemPerizinanBinding>(
            context,
            listData,
            ItemPerizinanBinding::inflate
        ){
    override fun bind(binding: ItemPerizinanBinding, item: DataXXXXXXXXXXXX) {
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

                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }


}