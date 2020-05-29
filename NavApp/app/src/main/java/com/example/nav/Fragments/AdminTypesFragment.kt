package com.example.nav.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nav.Adapters.TypesListAdapter
import com.example.nav.Adapters.TypesRecyclerAdapter
import com.example.nav.Entities.Types
import com.example.nav.Fragments_ViewModels.TypesViewModel
import com.example.nav.Helpers.TypesSwiper
import com.example.nav.Interfaces.IonRecyclerClickListener

import com.example.nav.R
import kotlinx.android.synthetic.main.fragment_admin_types.*

/**
 * A simple [Fragment] subclass.
 */
class AdminTypesFragment : Fragment(), IonRecyclerClickListener {

    private val model: TypesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_admin_types, container, false)
        val addCard: CardView = inf.findViewById(R.id.newTypeCard)
        val progressBar: ProgressBar = inf.findViewById(R.id.loadingAdminTypes)
        addCard.setOnClickListener {
            val fragment = TypesAddFragment()
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
        }
        //--------------------------------swipe to refresh------------------------------------------
        val refreshLayout: SwipeRefreshLayout = inf.findViewById(R.id.refreshTypesList)
        refreshLayout.setOnRefreshListener{
            model.getTypesData()
        }
        //-----------------------------swipe to refresh end-----------------------------------------

        viewModelObserving(refreshLayout, progressBar)
        if (model.typesList.value == null) {
            model.getTypesData()
        }

        return inf
    }

    private fun viewModelObserving(refreshLayout: SwipeRefreshLayout, progressBar: ProgressBar){
        //------------------------------view model observing----------------------------------------
        val listObserver = Observer<ArrayList<Types>> { newList ->
            val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val viewAdapter: RecyclerView.Adapter<*> = TypesRecyclerAdapter(newList, this)
            val recyclerView  = activity!!.findViewById<RecyclerView>(R.id.adminTypesList).apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }
            val itemTouchHelper = ItemTouchHelper(TypesSwiper(activity!!, model, "Bearer " + activity!!.intent.getStringExtra("Token")))
            itemTouchHelper.attachToRecyclerView(recyclerView)
            refreshLayout.isRefreshing = false
            progressBar.visibility = View.GONE
        }
        model.typesList.observe(this, listObserver)
        //----------------------------end of view model observing-----------------------------------
    }

    override fun onRecyclerItemClick(position: Int) {
        //not implemented
    }


}