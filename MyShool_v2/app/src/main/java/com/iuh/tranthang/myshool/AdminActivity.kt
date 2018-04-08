package com.iuh.tranthang.myshool

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.iuh.tranthang.myshool.ViewApdater.CustomAdapter
import com.iuh.tranthang.myshool.model.adm_display
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_inside.*

class AdminActivity : AppCompatActivity() {

    private var drawerLayout :DrawerLayout ?=null
    private var abdt: ActionBarDrawerToggle ?=null
    private var navigationView: NavigationView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        var token = getSharedPreferences("username", Context.MODE_PRIVATE)
        //var token_pw= getSharedPreferences("password",Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val intent= Intent(this,InsideActivity::class.java)
        //edit_password.setText(token_pw.getString("loginpassword"," "))
        var arrayInforMenu: ArrayList<adm_display> = ArrayList()

        arrayInforMenu.add(adm_display("Thông tin nhân viên", R.drawable.team_group))

        listview.adapter = CustomAdapter(this, arrayInforMenu)
        drawerLayout= findViewById(R.id.drawerLayout)
        navigationView= findViewById(R.id.menuNavigation)
        abdt= ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close)
        val drawerIndicatorEnabled = abdt!!.isDrawerIndicatorEnabled
        drawerLayout!!.addDrawerListener(abdt!!)
        abdt!!.syncState()
        navigationView!!.setNavigationItemSelectedListener(
                object : NavigationView.OnNavigationItemSelectedListener {
                    override fun onNavigationItemSelected(item: MenuItem): Boolean {
                        var boolean: Boolean?
                        if(item!!.itemId==R.id.DangXuat){
                            var token = getSharedPreferences("username", Context.MODE_PRIVATE)
                            var editor= token.edit()
                            editor.putString("loginusername"," ")
                            editor.commit()
                            startActivity(intent)
                            finish()
                            boolean=true
                        }else {
                            boolean = false
                        }
                        return boolean!!

                    }
                }
        )

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var boolean: Boolean?
        if(item!!.itemId==R.id.DangXuat){
            var token = getSharedPreferences("username", Context.MODE_PRIVATE)
            var editor= token.edit()
            editor.putString("loginusername"," ")
            editor.commit()
            var intent= Intent(this, InsideActivity::class.java)
            startActivity(intent)
            finish()
            boolean=true
        }else {
            boolean = super.onOptionsItemSelected(item)
        }
        return boolean!!

    }
}
