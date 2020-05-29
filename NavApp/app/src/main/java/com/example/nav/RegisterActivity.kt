package com.example.nav

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nav.Entities.User
import com.example.nav.Interfaces.IApi
import com.example.nav.MainActivity.Companion.BaseUrl
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register.setOnClickListener {
            if (registerPassword.text.toString() == registerPasswordRepeat.text.toString()) {
                checkInfo()
            }else {
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkInfo(){
        if (registerName.text.isNotEmpty() && registerSurname.text.isNotEmpty()){
            if (registerUsername.text.length > 3 && registerPassword.text.length > 3){
                postRegisterData()
            }else{
                Toast.makeText(this, "Username and password must be at least 4 letters long", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Please fill in all required spaces", Toast.LENGTH_SHORT).show()
        }
    }

    private fun postRegisterData(){
        val user = User()
        user.name = registerName.text.toString()
        user.surname = registerSurname.text.toString()
        user.username = registerUsername.text.toString()
        user.password = registerPassword.text.toString()
        user.role = "user"

        //------------------------------------------------------------------------------------------
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)

        val call: Call<User> = service.postUsers(user)
        call.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){
                    openLoginActivity()
                }
                else {
                    Toast.makeText(this@RegisterActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun openLoginActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}