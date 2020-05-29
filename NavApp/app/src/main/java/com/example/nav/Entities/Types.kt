package com.example.nav.Entities

import com.google.gson.annotations.SerializedName

class Types {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("goods")
    var goods: ArrayList<Types_Goods> = ArrayList()

    @SerializedName("specGoods")
    var specGoods: ArrayList<Goods> = ArrayList()
}