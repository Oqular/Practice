package com.example.nav.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.nav.Entities.User
import com.example.nav.Interfaces.IApi
import com.example.nav.MainActivity.Companion.BaseUrl

import com.example.nav.R
import kotlinx.android.synthetic.main.fragment_user_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 */
class UserProfileFragment : Fragment() {

    private lateinit var userOld: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_user_profile, container, false)
        //------------------------------------------------------------------------------------------
        val userButton: Button = inf.findViewById(R.id.userProfileButton)
        val progressBar: ProgressBar = inf.findViewById(R.id.loadingProfileInfo)
        val profileView: LinearLayout = inf.findViewById(R.id.profileInfo)
        //------------------------------------------------------------------------------------------
        getUserData(progressBar, profileView)
        userButton.setOnClickListener { updateInfo(inf) }
        return inf
    }

    private fun updateInfo(inf: View){
        val newPassword: EditText = inf.findViewById(R.id.userProfileEditPasswordEdit)
        val password: EditText = inf.findViewById(R.id.userProfilePasswordEdit1)
        val password2: EditText = inf.findViewById(R.id.userProfilePasswordEdit2)
        //------------------------------------------------------------------------------------------
        if (password.text.toString() == password2.text.toString()){
            if (password.text.toString() == userOld.password){
                if (newPassword.text.toString().length > 3){
//                    user.password = newPassword.text.toString()
                    editUserPassword(newPassword.text.toString())
                }else{
                    Toast.makeText(activity, "New password too short it should be at least 4 symbols long", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(activity!!, "password is incorrect", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(activity!!, "password doesn't match", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getUserData(progressBar: ProgressBar, profileView: LinearLayout){
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.getUserInfo(activity!!.intent.getIntExtra("userId", -1), "Bearer " + activity!!.intent.getStringExtra("Token"))
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 200) {
                    val resp = response.body()!!
                    userOld = resp
                    profileDisplay(userOld)
                    progressBar.visibility = View.GONE
                    profileView.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
            }
        })
    }

    private fun editUserPassword(pass: String){
        val user: User = userOld
        user.password = pass
        val retro = Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retro.create(IApi::class.java)
        val call =  service.editUser(activity!!.intent.getIntExtra("userId", -1), "Bearer " + activity!!.intent.getStringExtra("Token"), user)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {

                Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                if (response.code() == 204) {

                    Toast.makeText(context, "Password changed", Toast.LENGTH_SHORT).show()
                    profileDisplay(user)
                }
                else{
                    Toast.makeText(context, "Password was not changed", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun profileDisplay(user: User){
        userProfileName.text = "Name: " + user.name
        userProfileSurname.text = "Surname: " + user.surname
        userProfileUsername.text = "Username: " +user.username
    }


}
