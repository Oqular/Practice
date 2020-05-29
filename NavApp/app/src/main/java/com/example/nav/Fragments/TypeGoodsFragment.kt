package com.example.nav.Fragments


import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nav.Adapters.GoodsRecyclerAdapter
import com.example.nav.Entities.Goods
import com.example.nav.Entities.Types
import com.example.nav.Fragments_ViewModels.GoodsViewModel
import com.example.nav.Interfaces.IApi
import com.example.nav.Interfaces.IonRecyclerClickListener
import com.example.nav.MainActivity

import com.example.nav.R
import kotlinx.android.synthetic.main.fragment_type_goods.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 */
class TypeGoodsFragment : Fragment(), IonRecyclerClickListener {

    private lateinit var viewAdapter: GoodsRecyclerAdapter
    private lateinit var loadingBar: ProgressBar
    private lateinit var refreshLayout: SwipeRefreshLayout
    private var goodsList: ArrayList<Goods> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inf =inflater.inflate(R.layout.fragment_type_goods, container, false)
        refreshLayout = inf.findViewById(R.id.refreshTypeGoodsList)
        loadingBar = inf.findViewById(R.id.loadingTypeGoodsFragment)
        //------------------------------------------------------------------------------------------
        setHasOptionsMenu(true)
        val args: Bundle = arguments!!
        val infoId: Int = args.getInt("typeId")
        getGoodsByTypeData(infoId)
        //-------------setting refresh layout with which user can refresh manually------------------
        refreshLayout.setOnRefreshListener{
            getGoodsByTypeData(infoId)
        }
        //------------------------------end of refresh layout---------------------------------------
        return inf
    }

    private fun setGoodsList(){
        val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewAdapter = GoodsRecyclerAdapter(goodsList, this)
        activity!!.findViewById<RecyclerView>(R.id.typeGoodsList).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        refreshLayout.isRefreshing = false
        loadingBar.visibility = View.GONE
    }

    private fun getGoodsByTypeData(typeId: Int){
        val retro = Retrofit.Builder().baseUrl(MainActivity.BaseUrl).addConverterFactory(
            GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.getTypeById(typeId)
        call.enqueue(object : Callback<Types> {
            override fun onResponse(call: Call<Types>, response: Response<Types>) {
                if (response.code() == 200) {
                    val item: Types = response.body()!!

                    goodsList = item.specGoods
                    setGoodsList()
                }
                else{
                    Toast.makeText(activity!!, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Types>, t: Throwable) {
                Toast.makeText(activity!!, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onRecyclerItemClick(position: Int) {
        //------------------------------------------------------------------------------------------
        val item: Goods = goodsList[position]
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
