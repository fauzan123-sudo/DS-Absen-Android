package com.infinity.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ItemAktivitasBinding
import com.infinity.dsmabsen.databinding.ItemReimbursementBinding
import com.infinity.dsmabsen.model.DataXXXXXXX
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

class ReimbursementAdapter(
    private val context: Context,
    data: List<DataXXXXXXX>,
    private val onItemClick: (DataXXXXXXX) -> Unit
) :
    BaseAdapter<DataXXXXXXX, ItemReimbursementBinding>(
        context,
        data,
        ItemReimbursementBinding::inflate
    ) {
    override fun bind(binding: ItemReimbursementBinding, item: DataXXXXXXX) {
        binding.apply {
            item.apply {
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
//                    Toast.makeText(context, "iyo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}