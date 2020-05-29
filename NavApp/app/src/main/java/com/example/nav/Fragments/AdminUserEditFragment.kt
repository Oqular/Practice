package com.example.nav.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.example.nav.Fragments_ViewModels.UserViewModel

import com.example.nav.R

/**
 * A simple [Fragment] subclass.
 */
class AdminUserEditFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val model: UserViewModel by activityViewModels()
    private var infoPos: Int = -1
    private var chosenRole: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_admin_user_edit, container, false)
        val applyButton: Button = inf.findViewById(R.id.userApplyBtn)
        val deleteBtn: Button = inf.findViewById(R.id.userDeleteBtn)
        val spinner: Spinner = inf.findViewById(R.id.rolesSpinner)
        //------------------------------------------------------------------------------------------
        val token: String = "Bearer " + activity!!.intent.getStringExtra("Token")
        val progressBar: ProgressBar = inf.findViewById(R.id.userProgressBar)
        //-------------------------------args stuff-------------------------------------------------
        val args: Bundle = arguments!!
        infoPos = args.getInt("userPosition")
        val userName: TextView = inf.findViewById(R.id.adminUserName)
        val userSurname: TextView = inf.findViewById(R.id.adminUserSurname)
        userName.text = "Name: " + model.userList.value!![infoPos].name
        userSurname.text = "Surname: " + model.userList.value!![infoPos].surname
        //--------------------------------------end of args-----------------------------------------
        setUpSpinner(spinner)



        deleteBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            model.removeUser(model.userList.value!![infoPos], token, activity!!)
        }

        applyButton.setOnClickListener {
            model.editUserRole(infoPos, chosenRole, token, activity!!)
        }

        return inf
    }

    private fun getUserRoleIndex(): Int{
        for ((index, role) in resources.getStringArray(R.array.roles).withIndex()){
            if (role == model.userList.value!![infoPos].role){
                return index
            }
        }
        return -1
    }

    private fun setUpSpinner(spinner: Spinner){
        ArrayAdapter.createFromResource(
            activity!!,
            R.array.roles,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
        spinner.setSelection(getUserRoleIndex())
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        chosenRole = parent.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }
}
