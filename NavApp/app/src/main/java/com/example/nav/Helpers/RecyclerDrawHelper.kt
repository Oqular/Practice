package com.example.nav.Helpers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.R

class RecyclerDrawHelper {

    fun drawSwiper(c: Canvas, viewHolder: RecyclerView.ViewHolder, dX: Float, context: FragmentActivity){
        val deleteSwipe = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.RED, Color.DKGRAY))
        val editSwipe = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.DKGRAY, Color.GREEN))
        val background: GradientDrawable
        val icon: Drawable
        val itemView: View = viewHolder.itemView
        when {
            dX > 0 -> {//right
                icon = ContextCompat.getDrawable(context, R.drawable.ic_deleteicon)!!
                //-------
                val iconMargin: Int = (itemView.height - icon.intrinsicHeight) / 2
                val iconTop: Int = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                val iconBottom: Int = iconTop + icon.intrinsicHeight
                val iconLeft: Int = itemView.left + iconMargin / 2
                val iconRight: Int = itemView.left + iconMargin / 2 + icon.intrinsicHeight
                //-------
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background = deleteSwipe
                background.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
                background.draw(c)
                icon.draw(c)
            }
            dX < 0 -> {//left
                icon = ContextCompat.getDrawable(context, R.drawable.ic_editicon)!!
                //-------
                val iconMargin: Int = (itemView.height - icon.intrinsicHeight) / 2
                val iconTop: Int = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                val iconBottom: Int = iconTop + icon.intrinsicHeight
                val iconLeft: Int = itemView.right - iconMargin / 2 - icon.intrinsicHeight
                val iconRight: Int = itemView.right - iconMargin / 2
                //-------
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background = editSwipe
                background.setBounds(itemView.left, itemView.top,itemView.right, itemView.bottom)
                background.draw(c)
                icon.draw(c)
            }
            else -> {// unswiped
                //do nothing
            }
        }
    }
}