package com.infinity.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.databinding.ItemSallaryBinding
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXX

class SallaryAdapter(
    private val context: Context,
    private val listData: List<DataXXXXXXXXXXXXXXX>
) :
    RecyclerView.Adapter<SallaryAdapter.ViewHolder>() {
    var listener: RecyclerViewHandler? = null

    inner class ViewHolder(private val binding: ItemSallaryBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun setData(item: DataXXXXXXXXXXXXXXX) {
            binding.apply {
                with(item) {
                    tanggals.text = tanggal
                    sallary.text = "Rp$total"
                    idGaji.text = kode_payroll

                    itemView.setOnClickListener {
                        listener?.onItemSelected(listData[adapterPosition])
                    }
                }
            }
        }

        override fun onClick(p0: View?) {
            listener?.onItemSelected(listData[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemSallaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(listData[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = listData.size


}