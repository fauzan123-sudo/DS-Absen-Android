package com.infinity.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ItemRekapAbsensiBinding
import com.infinity.dsmabsen.helper.Helper
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXX

class AttendanceAdapter(private val context: Context, private val dataList: List<DataXXXXXXXXXXXXXXXXXXXXXXXXXX>) :
    RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRekapAbsensiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: DataXXXXXXXXXXXXXXXXXXXXXXXXXX) {
            binding.apply {
                with(item) {
                    val newFormat = "kk:mm:ss"
                    textView29.text = tanggal
                    val tanggal:String? = if (absen != null) Helper().convertTanggalBesengek(absen, newFormat) else "-"

                    textView31.text = tanggal!!

                    if (status == 1) {
                        imageView11.setImageResource(R.drawable.icon_checkin)
                        textView30.text = "Check In"
                        textView31.setTextColor(ContextCompat.getColor(context, R.color._success))

                    } else {
                        imageView11.setBackgroundResource(R.drawable.icon_checkout)
                        textView30.text = "Check out"
                        textView31.setTextColor(ContextCompat.getColor(context, R.color.danger))
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceAdapter.ViewHolder {
        val binding = ItemRekapAbsensiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceAdapter.ViewHolder, position: Int) {
        holder.setData(dataList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}
