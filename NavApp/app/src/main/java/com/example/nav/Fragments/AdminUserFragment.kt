package com.example.nav.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nav.Adapters.UserRecyclerAdapter
import com.example.nav.Entities.User
import com.example.nav.Fragments_ViewModels.UserViewModel
import com.example.nav.Interfaces.IonRecyclerClickListener

import com.example.nav.R

/**
 * A simple [Fragment] subclass.
 */
class AdminUserFragment : Fragment(), IonRecyclerClickListener {

    private val model: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_admin_user, container, false)
        //------------------------------------------------------------------------------------------
        val token: String = "Bearer " + activity!!.intent.getStringExtra("Token")
        val progressBar: ProgressBar = inf.findViewById(R.id.loadingAdminUser)
        //--------------------------------swipe to refresh------------------------------------------
        val refreshLayout: SwipeRefreshLayout = inf.findViewById(R.id.refreshUserList)
        refreshLayout.setOnRefreshListener{
            model.getUserData(token, activity!!)
        }
        //-----------------------------swipe to refresh end-----------------------------------------
        viewModelObserving(refreshLayout, progressBar)
        model.getUserData(token, activity!!)

        return inf
    }

    private fun viewModelObserving(refreshLayout: SwipeRefreshLayout, progressBar: ProgressBar){
        //------------------------------view model observing----------------------------------------
        val listObserver = Observer<ArrayList<User>> { newList ->
            val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val viewAdapter: RecyclerView.Adapter<*> = UserRecyclerAdapter(newList, this)
            activity!!.findViewById<RecyclerView>(R.id.adminUserList).apply {
                layoutManager = viewManager
                adapter = viewAdapter
                progressBar.visibility = View.GONE
            }

            refreshLayout.isRefreshing = false
        }
        model.userList.observe(this, listObserver)
        //----------------------------end of view model observing-----------------------------------
    }

    override fun onRecyclerItemClick(position: Int) {
        //------------------------------------------------------------------------------------------
        val fragment = AdminUserEditFragment()
        val args = Bundle()
        args.putInt("userPosition", position)
        fragment.arguments = args
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()

    }


}
