package com.iuh.tranthang.myshool

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.iuh.tranthang.myshool.ViewApdater.CustomAdapter
import com.iuh.tranthang.myshool.model.adm_display
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_inside.*

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var token = getSharedPreferences("username", Context.MODE_PRIVATE)
        var token_pw= getSharedPreferences("password",Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        edit_username.setText(token.getString("loginusername"," "))
        edit_password.setText(token_pw.getString("loginpassword"," "))
        var arrayInforMenu: ArrayList<adm_display> = ArrayList()

        arrayInforMenu.add(adm_display("Thông tin nhân viên", R.drawable.team_group))

        listview.adapter = CustomAdapter(this, arrayInforMenu)

    }
}
