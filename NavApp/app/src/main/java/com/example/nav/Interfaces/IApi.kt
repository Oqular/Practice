package com.example.nav.Interfaces


import com.example.nav.Entities.Goods
import com.example.nav.Entities.Login
import com.example.nav.Entities.Types
import com.example.nav.Entities.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface IApi {
    //-------------------------------------------GET------------------------------------------------
    @GET("api/goods")
    fun getGoods(): Call<ArrayList<Goods>>

    @GET("api/goods/{id}")
    fun getGoodsById(@Path("id") id: Int): Call<Goods>

    @GET("api/users/{id}/goods")
    fun getUserGoods(@Path("id") id: Int, @Header("Authorization") authToken: String): Call<ArrayList<Goods>>

    @GET("api/types")
    fun getTypes(): Call<ArrayList<Types>>

    @GET("api/types/{id}")
    fun getTypeById(@Path("id") id: Int): Call<Types>

    @GET("api/users/{id}")
    fun getUserInfo(@Path("id") id: Int, @Header("Authorization") authToken: String): Call<User>

    @GET("api/users")
    fun getUsers(@Header("Authorization") authToken: String): Call<ArrayList<User>>

    //------------------------------------------POST------------------------------------------------
    @Multipart
    @POST("api/goods")
    fun postGoods(@Header("Authorization") authToken: String, @Part("goods") goods: Goods, @Part image: ArrayList<MultipartBody.Part>): Call<Goods>

    @POST("api/login")
    fun login(@Body loginInfo: Login): Call<User>

    @POST("api/types")
    fun postType(@Header("Authorization") authToken: String, @Body types: Types): Call<Types>

    @POST("api/users")
    fun postUsers(@Body registerInfo: User): Call<User>

    //-----------------------------------------DELETE-----------------------------------------------
    @DELETE("api/goods/{id}")
    fun deleteGoods(@Path("id") id: Int, @Header("Authorization") authToken: String): Call<Goods>

    @DELETE("api/types/{id}")
    fun deleteType(@Path("id") id: Int, @Header("Authorization") authToken: String): Call<Types>

    @DELETE("api/users/{id}")
    fun deleteUser(@Path("id") id: Int, @Header("Authorization") authToken: String): Call<User>

    //------------------------------------------PUT-------------------------------------------------

    @PUT("api/types/{id}")
    fun editType(@Path("id") id: Int, @Header("Authorization") authToken: String, @Body types: Types): Call<Types>

    @PUT("api/users/{id}")
    fun editUser(@Path("id") id: Int, @Header("Authorization") authToken: String, @Body user: User): Call<User>

    @PUT("api/goods/{id}")
    fun editGoods(@Path("id") id: Int, @Header("Authorization") authToken: String, @Body goods: Goods): Call<Goods>

}