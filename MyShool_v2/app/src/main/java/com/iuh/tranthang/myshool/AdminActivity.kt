package com.iuh.tranthang.myshool

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.iuh.tranthang.myshool.ViewApdater.CustomAdapter
import com.iuh.tranthang.myshool.ViewApdater.ExpandableListAdapter
import com.iuh.tranthang.myshool.model.adm_display
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_inside.*

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var token = getSharedPreferences("username", Context.MODE_PRIVATE)
        //var token_pw= getSharedPreferences("password",Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        //edit_password.setText(token_pw.getString("loginpassword"," "))
        var arrayInforMenu: ArrayList<adm_display> = ArrayList()

        arrayInforMenu.add(adm_display("Thông tin nhân viên", R.drawable.team_group))

        listview.adapter = CustomAdapter(this, arrayInforMenu)
<<<<<<< HEAD


        val listHeader = listOf("number", "A")
        val numberList = listOf("1", "2", "3")
        val fruitsList = listOf("Thang", "Tam")
        val listChild = HashMap<String, List<String>>()
        listChild.put(listHeader[0], numberList)
        listChild.put(listHeader[1], fruitsList)

        val expandableListAdapter = ExpandableListAdapter(this, listHeader, listChild)

        expandable_list_view.setAdapter(expandableListAdapter)
=======
        btnLogOut.setOnClickListener {
            var editor= token.edit()
            editor.putString("loginusername"," ")
            editor.commit()
            var intent= Intent(this, InsideActivity::class.java)
            startActivity(intent)
            finish()
        }
>>>>>>> 5ba91c4de5a57d782525a278fa6d98cbeacde043
    }
}
