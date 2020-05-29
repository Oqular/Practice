package com.example.nav.Fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nav.Adapters.GoodsRecyclerAdapter
import com.example.nav.Entities.Goods
import com.example.nav.Fragments_ViewModels.GoodsViewModel
import com.example.nav.Helpers.UserGoodsSwiper
import com.example.nav.Interfaces.IonRecyclerClickListener

import com.example.nav.R

/**
 * A simple [Fragment] subclass.
 */
class UserGoodsFragment : Fragment(), IonRecyclerClickListener {

    private var token: String = "Not set"
    private lateinit var loadingBar: ProgressBar
    private lateinit var itemArray: ArrayList<Goods>
    private val model: GoodsViewModel by activityViewModels()
    private lateinit var viewAdapter: GoodsRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_user_goods, container, false)
        val userId: Int = activity!!.intent.getIntExtra("userId", -1)
        token = "Bearer " + activity!!.intent.getStringExtra("Token")
        loadingBar = inf.findViewById(R.id.loadingUserGoodsFragment)
        setHasOptionsMenu(true)
        //------------------------------------------------------------------------------------------

        if (model.userGoodsList.value == null) {
            model.getUserGoodsData(userId, token, activity!!)
        }else{
            loadingBar.visibility = View.GONE
        }

        val refreshLayout: SwipeRefreshLayout = inf.findViewById(R.id.refreshUserGoodsList)
        refreshLayout.setOnRefreshListener{
            model.getUserGoodsData(userId, token, activity!!)
        }
        viewModelObserving(refreshLayout)

        return inf
    }

    private fun viewModelObserving(refreshLayout: SwipeRefreshLayout){
        //------------------------------view model observing----------------------------------------
        val listObserver = Observer<ArrayList<Goods>> { newList ->
            // Update the UI, in this case, a TextView.
            val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            viewAdapter = GoodsRecyclerAdapter(newList, this)
            val recyclerView  = activity!!.findViewById<RecyclerView>(R.id.userGoodsList).apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }
            val itemTouchHelper = ItemTouchHelper(UserGoodsSwiper(activity!!, model, token))
            itemTouchHelper.attachToRecyclerView(recyclerView)
            refreshLayout.isRefreshing = false
            loadingBar.visibility = View.GONE
        }
        model.userGoodsList.observe(this, listObserver)
        //----------------------------end of view model observing-----------------------------------
    }

    override fun onRecyclerItemClick(position: Int) {
        //------------------------------------------------------------------------------------------
        itemArray = model.userGoodsList.value!!
        val item: Goods = itemArray[position]
        val fragment = GoodsDetailFragment()
        val args = Bundle()
        args.putInt("goodsId", item.id)
        fragment.arguments = args
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.navSearch)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewAdapter.filter.filter(newText)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }


}
