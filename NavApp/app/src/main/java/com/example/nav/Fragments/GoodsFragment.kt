package com.example.nav.Fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nav.Adapters.GoodsRecyclerAdapter
import com.example.nav.Entities.Goods
import com.example.nav.Fragments_ViewModels.GoodsViewModel
import com.example.nav.Interfaces.IonRecyclerClickListener
import com.example.nav.R
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class GoodsFragment : Fragment(), IonRecyclerClickListener {

    private var token: String = "Not set"
    private lateinit var loadingBar: ProgressBar
    private val model: GoodsViewModel by activityViewModels()
    private lateinit var viewAdapter: GoodsRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_goods, container, false)
        loadingBar = inf.findViewById(R.id.loadingGoodsFragment)
        setHasOptionsMenu(true)
        //------------------------------------------------------------------------------------------
        token = "Bearer " + activity!!.intent.getStringExtra("Token")
        //----------------------Show add button if logged in----------------------------------------
        val logged = activity!!.intent.getBooleanExtra("loggedIn", false)
        if (logged){
            val addButton: CardView = inf.findViewById(R.id.addGoodsCard)
            addButton.visibility = View.VISIBLE
            addButton.setOnClickListener{
                val fragment = GoodsAddFragment()
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()

            }
        }
        //-------------------------------End of add button------------------------------------------
        //-------------setting refresh layout with which user can refresh manually------------------
        val refreshLayout: SwipeRefreshLayout = inf.findViewById(R.id.refreshGoodsList)
        refreshLayout.setOnRefreshListener{
            model.getGoodsData()
        }
        //------------------------------end of refresh layout---------------------------------------
        viewModelObserving(refreshLayout)

        //------------------------Load data if its not yet loaded-----------------------------------
        if (model.goodsList.value == null) {
            model.getGoodsData()
        }else{
            loadingBar.visibility = View.GONE
        }
        //-------------------------------end of data loading----------------------------------------
        return inf
    }

    private fun viewModelObserving(refreshLayout: SwipeRefreshLayout){
        //------------------------------view model observing----------------------------------------
        val listObserver = Observer<ArrayList<Goods>> { newList ->
            // Update the UI, in this case, a TextView.
            val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            viewAdapter = GoodsRecyclerAdapter(newList, this@GoodsFragment)
            activity!!.findViewById<RecyclerView>(R.id.listGoods).apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }
            refreshLayout.isRefreshing = false
            loadingBar.visibility = View.GONE
        }
        model.goodsList.observe(this, listObserver)
        //----------------------------end of view model observing-----------------------------------
    }

    override fun onRecyclerItemClick(position: Int) {
        //------------------------------------------------------------------------------------------
        val itemArray = model.goodsList.value!!
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

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
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
