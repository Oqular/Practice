package com.example.nav.Adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcelable
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.Entities.Goods
import com.example.nav.Interfaces.IonRecyclerClickListener
import com.example.nav.R

class GoodsRecyclerAdapter (private val imgArray: ArrayList<Goods>, private val clickListener: IonRecyclerClickListener): RecyclerView.Adapter<GoodsRecyclerAdapter.MyViewHolder>(), Filterable {

    private val goodsArrayFull: ArrayList<Goods> = ArrayList(imgArray)

    class MyViewHolder(v: View, private val clickListener: IonRecyclerClickListener) : RecyclerView.ViewHolder(v), View.OnClickListener  {

        var img: ImageView = v.findViewById(R.id.goodsImg)
        var txt: TextView = v.findViewById(R.id.goodsTxt)


        init{
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onRecyclerItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val list = LayoutInflater.from(parent.context).inflate(R.layout.goods_list_row, parent, false)
        return MyViewHolder(list, clickListener)
    }

    override fun getItemCount() = imgArray.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (imgArray[position].images.size > 0) {
            val img: ByteArray = Base64.decode(imgArray[position].images[0].imageB, 0)//--------
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(img, 0, img.size, BitmapFactory.Options())//-------
            //------------------------------------------------------------------------------------------
            holder.img.setImageBitmap(bitmap)
        }else{
            holder.img.setImageResource(R.drawable.ic_noimgicon2)
        }

        holder.txt.text = imgArray[position].title
    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val filtered: ArrayList<Goods> = ArrayList()
                if (p0 == null || p0.isEmpty()){
                    filtered.addAll(goodsArrayFull)
                }else{
                    val filterPattern: String = p0.toString().toLowerCase().trim()
                    for (item in goodsArrayFull){
                        if (item.title!!.toLowerCase().contains(filterPattern)){
                            filtered.add(item)
                        }
                    }
                }
                val result = FilterResults()
                result.values = filtered
                return result
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                imgArray.clear()
                imgArray.addAll(p1!!.values as ArrayList<Goods>)
                notifyDataSetChanged()
            }

        }
    }


}