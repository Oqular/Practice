package com.example.nav.Helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.Adapters.GoodsRecyclerAdapter
import com.example.nav.Entities.Goods
import com.example.nav.Fragments.GoodsEditFragment
import com.example.nav.Fragments_ViewModels.GoodsViewModel
import com.example.nav.MainActivity
import com.example.nav.R

class UserGoodsSwiper(private val context: FragmentActivity, private val model: GoodsViewModel, private val token: String) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

//    var adapter: GoodsRecyclerAdapter = adapter

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position: Int = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            //left
            Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()
            val infoId: Int = model.userGoodsList.value!![position].id
            val fragment = GoodsEditFragment()
            val args = Bundle()
            args.putInt("goodsId", infoId)
            fragment.arguments = args
            context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
        }
        else if (direction == ItemTouchHelper.RIGHT) {
            //right
            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
            var array: ArrayList<Goods> = model.userGoodsList.value!!
            model.removeUserGoods(array[position], token, context)
            array.removeAt(position)
            model.userGoodsList.value = array
        }

    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val recyclerDrawer = RecyclerDrawHelper()
        recyclerDrawer.drawSwiper(c,viewHolder, dX, context)
    }
}