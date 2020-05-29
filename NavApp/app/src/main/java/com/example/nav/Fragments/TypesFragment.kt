package com.example.nav.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nav.Adapters.TypesRecyclerAdapter
import com.example.nav.Entities.Types
import com.example.nav.Fragments_ViewModels.TypesViewModel
import com.example.nav.Interfaces.IonRecyclerClickListener

import com.example.nav.R

/**
 * A simple [Fragment] subclass.
 */
class TypesFragment : Fragment(), IonRecyclerClickListener {

    private val model: TypesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_types, container, false)
        val refreshLayout: SwipeRefreshLayout = inf.findViewById(R.id.refreshTypesList)
        val progressBar: ProgressBar = inf.findViewById(R.id.loadingTypes)
        //------------------------------------------------------------------------------------------
        if (model.typesList.value == null){
            progressBar.visibility = View.VISIBLE
            model.getTypesData()
        }
        //-------------setting refresh layout with which user can refresh manually------------------
        refreshLayout.setOnRefreshListener{
            model.getTypesData()
        }
        //------------------------------end of refresh layout---------------------------------------
        viewModelObserving(refreshLayout, progressBar)

        return inf
    }

    private fun viewModelObserving(refreshLayout: SwipeRefreshLayout, progressBar: ProgressBar){
        //------------------------------view model observing----------------------------------------
        val listObserver = Observer<ArrayList<Types>> { newList ->
            // Update the UI, in this case, a TextView.
            val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val viewAdapter = TypesRecyclerAdapter(newList, this)
            activity!!.findViewById<RecyclerView>(R.id.typesList).apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }
            refreshLayout.isRefreshing = false
            progressBar.visibility = View.GONE
        }
        model.typesList.observe(this, listObserver)
        //----------------------------end of view model observing-----------------------------------
    }

    override fun onRecyclerItemClick(position: Int) {
        //------------------------------------------------------------------------------------------
        val itemArray = model.typesList.value!!
        val item: Types = itemArray[position]
        val fragment = TypeGoodsFragment()
        val args = Bundle()
        args.putInt("typeId", item.id)
        fragment.arguments = args
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
    }

}
