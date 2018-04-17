package com.iuh.tranthang.myshool

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.internal.NavigationMenu
import android.support.design.widget.NavigationView
import android.view.View
import android.widget.TextView
import org.w3c.dom.Text


class NavigationMenuActivity : AppCompatActivity() {
    private var navigationView: NavigationView? = null
    private var view :View ?= null
    private var txtDangXuat : Text ?= null
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        var token = getSharedPreferences("username", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.menu.navigation_menu)
        navigationView= findViewById<NavigationView>(R.id.menuNavigation) as NavigationView
        view = navigationView!!.getHeaderView(0)
        navigationView!!.setOnClickListener { view ->
            when(view.id){
                R.id.DangXuat->{
                    var editor= token.edit()
                    editor.putString("loginusername"," ")
                    editor.commit()
                    var intent= Intent(this, InsideActivity::class.java)
                    startActivity(intent)
                    }
                R.id.itemTrangCaNhan->{
                    var intent= Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }
}
