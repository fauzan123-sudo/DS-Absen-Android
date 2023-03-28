package com.example.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.ItemPendidikanBinding
import com.example.dsmabsen.databinding.ItemRekapAbsensiBinding
import com.example.dsmabsen.helper.Helper
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXX
import com.example.dsmabsen.model.Pendidikan

class PendidikanAdapter(private val context: Context) :
    RecyclerView.Adapter<PendidikanAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPendidikanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: Pendidikan) {
            binding.apply {
                with(item) {
                    tvNamaPendidikan.text = pendidikan.nama
                    tvNamaJurusan.text = jurusan!!.nama
                    tvNamaSekolah.text = nama_sekolah
                    tvNomorIjazah.text = nomor_ijazah
                    tvTanggalLulus.text = tanggal_lulus

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemPendidikanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<Pendidikan>() {
        override fun areItemsTheSame(oldItem: Pendidikan, newItem: Pendidikan): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pendidikan, newItem: Pendidikan): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}