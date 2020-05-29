package com.example.nav.Entities

import com.google.gson.annotations.SerializedName

class Login {
    @SerializedName("username")
    var username: String? = ""

    @SerializedName("password")
    var password: String? = ""
}