package com.example.nav.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.Entities.Types
import com.example.nav.Interfaces.IonRecyclerClickListener
import com.example.nav.R

class TypesRecyclerAdapter(private val typesArray: ArrayList<Types>, private val clickListener: IonRecyclerClickListener): RecyclerView.Adapter<TypesRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(v: View, private val clickListener: IonRecyclerClickListener) : RecyclerView.ViewHolder(v), View.OnClickListener  {

        var txt: TextView = v.findViewById(R.id.typesListTxt)


        init{
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onRecyclerItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val list = LayoutInflater.from(parent.context).inflate(R.layout.types_list_row, parent, false)
        return MyViewHolder(list, clickListener)
    }

    override fun getItemCount() = typesArray.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txt.text = typesArray[position].name
    }
}