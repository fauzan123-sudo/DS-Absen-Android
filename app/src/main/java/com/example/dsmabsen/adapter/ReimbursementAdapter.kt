package com.example.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.ItemRekapAbsensiBinding
import com.example.dsmabsen.helper.Helper
import com.example.dsmabsen.model.DataXXXXXXXXXX

class ReimbursementAdapter(private val context: Context) :
    RecyclerView.Adapter<ReimbursementAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRekapAbsensiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: DataXXXXXXXXXX) {
            binding.apply {
                with(item) {
                    val newFormat = "kk:mm:ss"
                    textView29.text = tanggal
                    textView31.text = Helper().convertTanggal(absen!!, newFormat)

                    if (status == 1) {
                        imageView11.setImageResource(R.drawable.round_icon1)
                        textView30.text = "Check In"
                        textView31.setTextColor(ContextCompat.getColor(context, R.color._success))

                    } else {
                        imageView11.setBackgroundResource(R.drawable.round_icon2)
                        textView30.text = "Check out"
                        textView31.setTextColor(ContextCompat.getColor(context, R.color.danger))
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemRekapAbsensiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<DataXXXXXXXXXX>() {
        override fun areItemsTheSame(oldItem: DataXXXXXXXXXX, newItem: DataXXXXXXXXXX): Boolean {
            return oldItem.absen == newItem.absen
        }

        override fun areContentsTheSame(oldItem: DataXXXXXXXXXX, newItem: DataXXXXXXXXXX): Boolean {
            return oldItem.absen == newItem.absen
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}