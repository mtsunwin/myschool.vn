package com.iuh.tranthang.myshool

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.iuh.tranthang.myshool.ViewApdater.ExpandableListAdapter
import com.iuh.tranthang.myshool.model.adm_display
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var token = getSharedPreferences("username", Context.MODE_PRIVATE)
        //var token_pw= getSharedPreferences("password",Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        //edit_password.setText(token_pw.getString("loginpassword"," "))

        /* customer list view
        var arrayInforMenu: ArrayList<adm_display> = ArrayList()
        arrayInforMenu.add(adm_display("Thông tin nhân viên", R.drawable.team_group))
        listview.adapter = CustomAdapter(this, arrayInforMenu)*/


        val listHeader: ArrayList<adm_display> = ArrayList()
        listHeader.add(adm_display("Thông tin nhân viên", R.drawable.team_group))
        listHeader.add(adm_display("Quản lý thông báo", R.drawable.team_group))

        val inforStaff: ArrayList<adm_display> = ArrayList()
        inforStaff.add(adm_display("Danh sách tài khoản", R.drawable.team_group))
        inforStaff.add(adm_display("Thêm tài khoản", R.drawable.team_group))
        inforStaff.add(adm_display("Xóa nhân viên", R.drawable.team_group))

        val inforNotify: ArrayList<adm_display> = ArrayList()
        inforNotify.add(adm_display("Danh sách thông báo", R.drawable.team_group))
        inforNotify.add(adm_display("Tạo thông báo", R.drawable.team_group))

        val fruitsList = listOf("Thang", "Tam")
        val listChild = HashMap<String, ArrayList<adm_display>>()

        listChild.put(listHeader[0].getName(), inforStaff)
        listChild.put(listHeader[1].getName(), inforNotify)

        val expandableListAdapter = ExpandableListAdapter(this, listHeader, listChild)

        expandable_list_view.setAdapter(expandableListAdapter)

        btnLogOut.setOnClickListener {
            var editor = token.edit()
            editor.putString("loginusername", " ")
            editor.commit()
            var intent = Intent(this, InsideActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
