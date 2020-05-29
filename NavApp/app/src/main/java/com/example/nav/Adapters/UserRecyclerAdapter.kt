package com.example.nav.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.Entities.User
import com.example.nav.Interfaces.IonRecyclerClickListener
import com.example.nav.R

class UserRecyclerAdapter(private val userArray: ArrayList<User>, private val clickListener: IonRecyclerClickListener): RecyclerView.Adapter<UserRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(v: View, private val clickListener: IonRecyclerClickListener) : RecyclerView.ViewHolder(v), View.OnClickListener  {

        var txt: TextView = v.findViewById(R.id.userListTxt)
        var lvl: TextView = v.findViewById(R.id.userListLvl)


        init{
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onRecyclerItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val list = LayoutInflater.from(parent.context).inflate(R.layout.user_list_row, parent, false)
        return MyViewHolder(list, clickListener)
    }

    override fun getItemCount() = userArray.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txt.text = userArray[position].name + " " + userArray[position].surname
        holder.lvl.text = userArray[position].role
    }
}