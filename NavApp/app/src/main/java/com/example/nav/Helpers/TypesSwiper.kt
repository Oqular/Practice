package com.example.nav.Helpers

import android.graphics.Canvas
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.nav.Entities.Types
import com.example.nav.Fragments.TypesEditFragment
import com.example.nav.Fragments_ViewModels.TypesViewModel
import com.example.nav.R
import kotlinx.android.synthetic.main.content_view.view.*

class TypesSwiper(private val context: FragmentActivity, private val model: TypesViewModel, private val token: String) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)  {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position: Int = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            //left
            Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()
            val fragment = TypesEditFragment()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
        }
        else if (direction == ItemTouchHelper.RIGHT) {
            //right
            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
            val array: ArrayList<Types> = model.typesList.value!!
            model.removeType(array[position], token, context)
        }

    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val recyclerDrawer = RecyclerDrawHelper()
        recyclerDrawer.drawSwiper(c,viewHolder, dX, context)
    }
}