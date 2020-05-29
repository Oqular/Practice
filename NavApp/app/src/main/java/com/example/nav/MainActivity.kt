package com.example.nav

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat
import com.example.nav.Fragments.*
import com.example.nav.Fragments_ViewModels.GoodsViewModel
import com.example.nav.Fragments_ViewModels.TypesViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val modelTypes: TypesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Setting goods fragment as a main fragment that u see when u open the app
        if (savedInstanceState == null){
            val fragment = GoodsFragment()  //Main Page
            supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).commit()
        }

        setUpNavigator()

        modelTypes.getTypesData()

    }

    private fun setUpNavigator(){
        //-----------Access navigation header first before accessing its items------------
        val navHeader: View = navView.getHeaderView(0)
        val loginButton: Button = navHeader.findViewById(R.id.drawerLoginButton)
        loginButton.setOnClickListener { login() }

        val userMail: TextView = navHeader.findViewById(R.id.userName)
        userMail.text = intent.getStringExtra("userName")
        userMail.visibility = View.VISIBLE

        val userInfo: TextView = navHeader.findViewById(R.id.userSurname)
        userInfo.text = intent.getStringExtra("userName")
        userInfo.visibility = View.VISIBLE
        //-------------------Navigation header work end-----------------------------------
        //-------------------------Show Login stuff if logged in------------------------
        val logged = intent.getBooleanExtra("loggedIn", false)
        if (logged){
            val menu: Menu = navView.menu
            val userMenuItem: MenuItem = menu.findItem(R.id.loginStuff)
            userMenuItem.isVisible = true
            loginButton.visibility = View.INVISIBLE
            //-------------------------show admin stuff if admin------------------------------------
            val role = intent.getStringExtra("role")
            if (role == "admin"){
                val adminMenuItem: MenuItem = menu.findItem(R.id.adminStuff)
                adminMenuItem.isVisible = true
            }
        }
        //---------------------------------End of login stuff---------------------------

        //-----------------------------Drawer--------------------------------------------
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.navView)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, 0, 0)

        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.navGoods -> {
                    // handle click
                    Toast.makeText(this, "Goods", Toast.LENGTH_SHORT).show()
                    val fragment = GoodsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
                    drawer.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.navRent -> {
                    Toast.makeText(this, "Types", Toast.LENGTH_SHORT).show()
                    val fragment = TypesFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
                    drawer.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.navUserProfile -> {
                    val fragment = UserProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
                    drawer.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.navUserGoods -> {
                    val fragment = UserGoodsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
                    drawer.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.navAddGoods -> {
                    val fragment = GoodsAddFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
                    drawer.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.navAdminTypes -> {
                    val fragment = AdminTypesFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
                    drawer.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.navAdminAddTypes ->{
                    val fragment = TypesAddFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
                    drawer.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.navAdminManageUser ->{
                    val fragment = AdminUserFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit()
                    drawer.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.navLogOut -> {
                    val intent = Intent(this, MainActivity::class.java)//reset main activity so it wouldn't have any info saved in it
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        //---------------------------Drawer end------------------------------------------
    }

    private fun login(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        //Back to the previous fragment
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStackImmediate()
    }

    companion object {
        const val BaseUrl = "http://practiceshop.gearhostpreview.com"
    }
}
