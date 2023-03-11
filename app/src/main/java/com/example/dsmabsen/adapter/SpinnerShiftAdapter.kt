package com.example.dsmabsen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.dsmabsen.R
import com.example.dsmabsen.model.DataXXXXXXXXXXXXX
import com.example.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXX

class SpinnerShiftAdapter(context: Context, list: List<DataXXXXXXXXXXXXXXXXXXXXXXX>) :
    ArrayAdapter<DataXXXXXXXXXXXXXXXXXXXXXXX>(context, 0, list) {

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

        list.let {
            val textPermission = view.findViewById<TextView>(R.id.permissions)
            textPermission.text = list!!.label
        }

        return view
    }
}
