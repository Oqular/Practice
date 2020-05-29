package com.example.nav.Fragments_ViewModels

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nav.Entities.Goods
import com.example.nav.Entities.TempImg
import com.example.nav.Fragments.GoodsFragment
import com.example.nav.Fragments.UserGoodsFragment
import com.example.nav.Interfaces.IApi
import com.example.nav.MainActivity
import com.example.nav.MainActivity.Companion.BaseUrl
import com.example.nav.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class GoodsViewModel: ViewModel() {

    val goodsList: MutableLiveData<ArrayList<Goods>> by lazy {
        MutableLiveData<ArrayList<Goods>>()
    }

    val userGoodsList: MutableLiveData<ArrayList<Goods>> by lazy {
        MutableLiveData<ArrayList<Goods>>()
    }

    //calling http request DELETE goods method
    fun removeUserGoods(item: Goods, token: String, context: FragmentActivity){
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
            GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.deleteGoods(item.id, token)
        call.enqueue(object : Callback<Goods> {
            override fun onResponse(call: Call<Goods>, response: Response<Goods>) {
                if (response.code() == 200) {
                    getGoodsData()
                }
                else{
                    Toast.makeText(context, "Something went wrong"  + response.code().toString(), Toast.LENGTH_SHORT).show()
                    val fragment = UserGoodsFragment()
                    context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
                }
            }
            override fun onFailure(call: Call<Goods>, t: Throwable) {
                Toast.makeText(context, "Something went wrong try to check your internet connection", Toast.LENGTH_SHORT).show()
                val fragment = UserGoodsFragment()
                context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
            }
        })
    }

    //calling http request GET goods method
    fun getGoodsData() {
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.getGoods()
        call.enqueue(object : Callback<ArrayList<Goods>> {
            override fun onResponse(call: Call<ArrayList<Goods>>, response: Response<ArrayList<Goods>>) {
                if (response.code() == 200) {
                    goodsList.value = response.body()!!
                }
            }
            override fun onFailure(call: Call<ArrayList<Goods>>, t: Throwable) {
            }
        })
    }

    //calling http request GET goods method with specified id
    fun getUserGoodsData(userId: Int, token: String, context: FragmentActivity) {
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(
            GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call = service.getUserGoods(userId, token)
        call.enqueue(object : Callback<ArrayList<Goods>> {
            override fun onResponse(call: Call<ArrayList<Goods>>, response: Response<ArrayList<Goods>>) {
                if (response.code() == 200) {
                    userGoodsList.value = response.body()!!
                }
                else
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<ArrayList<Goods>>, t: Throwable) {
                Toast.makeText(context, "Failure", Toast.LENGTH_LONG).show()
            }
        })
    }


    fun postGoodsData(item: Goods, imgArray: ArrayList<TempImg>, token: String, context: FragmentActivity, progBar: ProgressBar){
        //-----------------------------------------------------------------------------------------------------------------
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)

        val fileArray: ArrayList<File> = ArrayList()
        val imageArray: ArrayList<MultipartBody.Part> = ArrayList()
        for (i in imgArray){
            val file = File(i.path)
            val requestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val imgFile: MultipartBody.Part = MultipartBody.Part.createFormData("images", file.name, requestBody)
            fileArray.add(file)
            imageArray.add(imgFile)
        }
        //-------------------------call Post method and add items-----------------------------------
        val call: Call<Goods> = service.postGoods(token, item, imageArray)
        call.enqueue(object: Callback<Goods> {
            override fun onResponse(call: Call<Goods>, response: Response<Goods>) {
                if (!response.isSuccessful){
                    Toast.makeText(context, "Code; " + response.code(), Toast.LENGTH_SHORT).show()
                    progBar.visibility = View.GONE
                }
                else {
                    val fragment = GoodsFragment()
                    context.supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
                    getGoodsData()
                }
            }
            override fun onFailure(call: Call<Goods>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                progBar.visibility = View.GONE
            }
        })
    }

    fun editGoods(item: Goods, token: String, context: FragmentActivity, progBar: ProgressBar){
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.editGoods(item.id, token, item)
        call.enqueue(object : Callback<Goods> {
            override fun onResponse(call: Call<Goods>, response: Response<Goods>) {
                if (response.code() == 204) {
                    context.supportFragmentManager.popBackStackImmediate()
                }else{
                    Toast.makeText(context, "The item no longer exist", Toast.LENGTH_SHORT).show()
                    context.supportFragmentManager.popBackStackImmediate()
                }
            }
            override fun onFailure(call: Call<Goods>, t: Throwable) {
                progBar.visibility = View.GONE
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }
}