package com.example.nav.Entities

import com.google.gson.annotations.SerializedName

class   Goods {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("title")
    var title: String? = ""

    @SerializedName("description")
    var description: String? = ""

    @SerializedName("phone")
    var phone: String? = ""

    @SerializedName("address")
    var address: String? = ""

    @SerializedName("type")
    var type: ArrayList<Types_Goods> = ArrayList()

    @SerializedName("seller")
    var seller: String? = ""

    @SerializedName("userId")
    var userId: Int? = 0

    @SerializedName("images")
    var images: ArrayList<Image> = ArrayList()
}