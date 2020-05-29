package com.example.nav.Entities

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = ""

    @SerializedName("surname")
    var surname: String? = ""

    @SerializedName("rating")
    var rating: Float = 0.0f

    @SerializedName("goods")
    var goods: ArrayList<Goods> = ArrayList()

    @SerializedName("username")
    var username: String? = ""

    @SerializedName("password")
    var password: String? = ""

    @SerializedName("role")
    var role: String? = ""

    @SerializedName("token")
    var token: String? =""
}