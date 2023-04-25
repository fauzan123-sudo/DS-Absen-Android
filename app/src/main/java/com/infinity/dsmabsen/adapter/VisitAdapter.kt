package com.infinity.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.databinding.ItemVisitBinding
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXX
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

class VisitAdapter(
    private val context: Context,
    private val listData: List<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX>,
    private val onItemClick: (DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX) -> Unit
) :
    BaseAdapter<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX, ItemVisitBinding>(
        context,
        listData,
        ItemVisitBinding::inflate
    ) {
    override fun bind(binding: ItemVisitBinding, item: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX) {
        binding.apply {
            with(item) {
                val newFormat = "kk:mm:ss"
                tvTanggalVisit.text = tanggal
                tvJamVisit.text = jam
                tvKeteranganVisit.text = visit

                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }


}