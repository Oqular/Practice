package com.example.nav.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.Entities.TempImg
import com.example.nav.Interfaces.IonRecyclerClickListener
import com.example.nav.R

class TempImageRecyclerAdapter(private val imgArray: ArrayList<TempImg>, private val clickListener: IonRecyclerClickListener): RecyclerView.Adapter<TempImageRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(v: View, private val clickListener: IonRecyclerClickListener) : RecyclerView.ViewHolder(v), View.OnClickListener {

        var img: ImageView = v.findViewById(R.id.imageItem)

        init{
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onRecyclerItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val image = LayoutInflater.from(parent.context).inflate(R.layout.image_list_row, parent, false)
        return MyViewHolder(image, clickListener)
    }

    override fun getItemCount() = imgArray.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.img.setImageBitmap(imgArray[position].image)

    }



}