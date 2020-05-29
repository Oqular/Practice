package com.example.nav.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.nav.Entities.Types
import com.example.nav.R

class TypesListAdapter(context: Context, private val imgArray: ArrayList<Types>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return imgArray.size
    }

    override fun getItem(position: Int): Any {
        return imgArray[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowV = inflater.inflate(R.layout.add_types_list_row, parent, false)

        val textV = rowV.findViewById(R.id.typesListTxt) as TextView
        val item = getItem(position) as Types
        textV.text = item.name
        return rowV
    }
}