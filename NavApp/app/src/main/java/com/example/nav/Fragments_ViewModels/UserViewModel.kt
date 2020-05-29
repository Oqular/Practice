package com.example.nav.Fragments_ViewModels

import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nav.Entities.User
import com.example.nav.Fragments.AdminUserFragment
import com.example.nav.Interfaces.IApi
import com.example.nav.MainActivity.Companion.BaseUrl
import com.example.nav.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserViewModel: ViewModel() {

    val userList: MutableLiveData<ArrayList<User>> by lazy {
        MutableLiveData<ArrayList<User>>()
    }

    private fun removeSelf(context: FragmentActivity){
        val uid: Int = context.intent.getIntExtra("userId", -1)
        for ((index, u) in userList.value!!.withIndex()){
            if (u.id == uid){
                userList.value!!.removeAt(index)
                break
            }
        }
    }

    fun getUserData(token: String, context: FragmentActivity){
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call = service.getUsers(token)
        call.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if (response.code() == 200) {
                    val resp = response.body()!!
                    userList.value = resp
                    removeSelf(context)
                }else{
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun removeUser(item: User, token: String, context: FragmentActivity){
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
            GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.deleteUser(item.id, token)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 200) {
                    getUserData(token, context)
                    val fragment = AdminUserFragment()
                    context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()

                }
                else if (response.code() == 403){
                    Toast.makeText(context, "You don't have permission to do that"  + response.code().toString(), Toast.LENGTH_SHORT).show()
                    val fragment = AdminUserFragment()
                    context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(context, "Something went wrong try to check your internet connection", Toast.LENGTH_SHORT).show()
                val fragment = AdminUserFragment()
                context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
            }
        })
    }

    fun editUserRole(infoPos: Int, role: String, token: String, context: FragmentActivity){
        val user: User = userList.value!![infoPos]
        user.role = role
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.editUser(user.id, token, user)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 204) {
                    val fragment = AdminUserFragment()
                    context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
                }
                else{
                    Toast.makeText(context, "Role was not changed", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }
}