package com.infinity.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

class SpinnerVisitAdapter(context: Context, list: List<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX>) :
    ArrayAdapter<DataXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return myView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return myView(position, convertView, parent)
    }

    private fun myView(position: Int, convertView: View?, parent: ViewGroup): View {
        val list = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_spinner, parent, false
        )

        if (position == 0) {
            val textPermission = view.findViewById<TextView>(R.id.permissions)
            textPermission.text = "Pilih Lokasi Visit"
            textPermission.setTextColor(ContextCompat.getColor(context, R.color._grey))
        } else {
            list.let {
                val textPermission = view.findViewById<TextView>(R.id.permissions)
                textPermission.text = list!!.nama
                textPermission.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }

        return view
    }
}






