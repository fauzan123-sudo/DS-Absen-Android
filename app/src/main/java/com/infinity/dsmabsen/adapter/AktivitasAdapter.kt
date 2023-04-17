package com.infinity.dsmabsen.adapter

import android.content.Context
import android.widget.Toast
import com.bumptech.glide.Glide
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.ItemAktivitasBinding
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

class AktivitasAdapter(
    private val context: Context,
    val data: List<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX>,
    private val onItemClick: (DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX) -> Unit
) :
    BaseAdapter<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX, ItemAktivitasBinding>(
        context,
        data,
        ItemAktivitasBinding::inflate
    ) {

    override fun bind(
        binding: ItemAktivitasBinding,
        item: DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    ) {
        with(item) {
            with(binding) {
                tvNama.text = nama
                if (foto.isEmpty()) {
                    Glide.with(context)
                        .load(foto)
                        .placeholder(R.drawable.progress_animation)
                        .into(ivUser)
                } else {
                    Glide.with(context)
                        .load(foto)
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.image_not_found)
                        .into(ivUser)
                }
                tvTanggalAktivitas.text = created_at

                root.setOnClickListener {
                    onItemClick(item)
//                    Toast.makeText(context, "iyo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}