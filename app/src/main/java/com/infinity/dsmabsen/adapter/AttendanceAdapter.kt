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
import com.infinity.dsmabsen.model.DataXXXXXXX
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXX

class AttendanceAdapter(
    private val context: Context, private val dataList: List<DataXXXXXXXXXXXXXXXXXXXXXXXXXX>,
    private val onItemClick: (DataXXXXXXXXXXXXXXXXXXXXXXXXXX) -> Unit
) :
    BaseAdapter<DataXXXXXXXXXXXXXXXXXXXXXXXXXX, ItemRekapAbsensiBinding>(
        context,
        dataList,
        ItemRekapAbsensiBinding::inflate
    ) {
    override fun bind(binding: ItemRekapAbsensiBinding, item: DataXXXXXXXXXXXXXXXXXXXXXXXXXX) {
        binding.apply {
            with(item) {
                val newFormat = "kk:mm:ss"
                textView29.text = tanggal
                val tanggal: String? =
                    if (absen != null) Helper().convertTanggalBesengek(absen, newFormat) else "-"

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

                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

}
