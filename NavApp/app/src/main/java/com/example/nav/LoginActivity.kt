package com.example.nav

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nav.Entities.Login
import com.example.nav.Entities.User
import com.example.nav.Interfaces.IApi
import com.example.nav.MainActivity.Companion.BaseUrl
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn.setOnClickListener { postLoginData() }
        registerBtn.setOnClickListener {
            openRegisterActivity()
        }
    }

    private fun postLoginData() {
        val loginInfo = Login()
        loginInfo.username = usernameInput.text.toString()
        loginInfo.password = passwordInput.text.toString()
        //-----------------------------------------------------------------------------------------------------------------
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
//
        val call: Call<User> = service.login(loginInfo)
        call.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (!response.isSuccessful){
                    Toast.makeText(this@LoginActivity, "Password or Username is incorrect", Toast.LENGTH_SHORT).show()
                }
                else {
                    val user: User = response.body()!!
                    openMainActivity(user)
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun openMainActivity(user: User){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userId", user.id)
        intent.putExtra("userName", user.name.toString() +user.surname.toString())
        intent.putExtra("Token", user.token.toString())
        intent.putExtra("role", user.role.toString())
        intent.putExtra("loggedIn", true)
        startActivity(intent)
    }

    private fun openRegisterActivity(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}