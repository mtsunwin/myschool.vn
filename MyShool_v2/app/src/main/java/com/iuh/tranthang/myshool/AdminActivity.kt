package com.iuh.tranthang.myshool

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.iuh.tranthang.myshool.ViewApdater.CustomAdapter
import com.iuh.tranthang.myshool.ViewApdater.ExpandableListAdapter
import com.iuh.tranthang.myshool.model.adm_display
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        var arrayInforMenu: ArrayList<adm_display> = ArrayList()

        arrayInforMenu.add(adm_display("Thông tin nhân viên", R.drawable.team_group))

        listview.adapter = CustomAdapter(this, arrayInforMenu)


        val listHeader = listOf("number", "A")
        val numberList = listOf("1", "2", "3")
        val fruitsList = listOf("Thang", "Tam")
        val listChild = HashMap<String, List<String>>()
        listChild.put(listHeader[0], numberList)
        listChild.put(listHeader[1], fruitsList)

        val expandableListAdapter = ExpandableListAdapter(this, listHeader, listChild)

        expandable_list_view.setAdapter(expandableListAdapter)
    }
}
