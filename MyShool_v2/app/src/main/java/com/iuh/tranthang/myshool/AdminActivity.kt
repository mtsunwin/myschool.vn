package com.iuh.tranthang.myshool

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.iuh.tranthang.myshool.ViewApdater.CustomAdapter
import com.iuh.tranthang.myshool.model.adm_display
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        var arrayInforMenu: ArrayList<adm_display> = ArrayList()

        arrayInforMenu.add(adm_display("Thông tin nhân viên", R.drawable.team_group))

        listview.adapter = CustomAdapter(this, arrayInforMenu)

    }
}
