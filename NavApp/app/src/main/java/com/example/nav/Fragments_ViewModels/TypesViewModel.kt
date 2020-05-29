package com.example.nav.Fragments_ViewModels

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nav.Entities.Types
import com.example.nav.Fragments.AdminTypesFragment
import com.example.nav.Interfaces.IApi
import com.example.nav.MainActivity.Companion.BaseUrl
import com.example.nav.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TypesViewModel: ViewModel() {

    val typesList: MutableLiveData<ArrayList<Types>> by lazy {
        MutableLiveData<ArrayList<Types>>()
    }

    fun getTypesData(){
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call = service.getTypes()
        call.enqueue(object : Callback<ArrayList<Types>> {
            override fun onResponse(call: Call<ArrayList<Types>>, response: Response<ArrayList<Types>>) {
                if (response.code() == 200) {
                    val resp = response.body()!!
                    typesList.value = resp
                }
            }
            override fun onFailure(call: Call<ArrayList<Types>>, t: Throwable) {
            }
        })
    }

    fun removeType(item: Types, token: String, context: FragmentActivity){
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.deleteType(item.id, token)
        call.enqueue(object : Callback<Types> {
            override fun onResponse(call: Call<Types>, response: Response<Types>) {
                if (response.code() == 200) {
                    getTypesData()
                }
                else if (response.code() == 403){
                    Toast.makeText(context, "You don't have permission to do that"  + response.code().toString(), Toast.LENGTH_SHORT).show()
                    val fragment = AdminTypesFragment()
                    context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
                }
            }
            override fun onFailure(call: Call<Types>, t: Throwable) {
                Toast.makeText(context, "Something went wrong try to check your internet connection", Toast.LENGTH_SHORT).show()
                val fragment = AdminTypesFragment()
                context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
            }
        })
    }

    fun editType(item: Types, token: String, context: FragmentActivity){
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.editType(item.id, token, item)
        call.enqueue(object : Callback<Types> {
            override fun onResponse(call: Call<Types>, response: Response<Types>) {
                if (response.code() == 200) {
                    getTypesData()//update types list after editing
                }
                else if (response.code() == 403){
                    Toast.makeText(context, "You don't have permission to do that", Toast.LENGTH_SHORT).show()
                    val fragment = AdminTypesFragment()
                    context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
                }
            }
            override fun onFailure(call: Call<Types>, t: Throwable) {
                Toast.makeText(context, "Something went wrong try to check your internet connection", Toast.LENGTH_SHORT).show()
                val fragment = AdminTypesFragment()
                context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
            }
        })
    }

    fun addType(item: Types, token: String, context: FragmentActivity){
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.postType(token, item)
        call.enqueue(object : Callback<Types> {
            override fun onResponse(call: Call<Types>, response: Response<Types>) {
                if (response.code() == 200) {
                    getTypesData()//update types list after adding
                }
                else if (response.code() == 403){
                    Toast.makeText(context, "You don't have permission to do that", Toast.LENGTH_SHORT).show()
                    val fragment = AdminTypesFragment()
                    context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
                }
            }
            override fun onFailure(call: Call<Types>, t: Throwable) {
                Toast.makeText(context, "Something went wrong try to check your internet connection", Toast.LENGTH_SHORT).show()
                val fragment = AdminTypesFragment()
                context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
            }
        })
    }

    fun typeExist(name: String): Boolean{
        for (type in typesList.value!!){
            if (type.name.toLowerCase() == name.toLowerCase()){
                return true
            }
        }
        return false
    }
}