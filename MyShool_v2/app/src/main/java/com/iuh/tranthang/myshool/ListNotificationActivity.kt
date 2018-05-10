package com.iuh.tranthang.myshool

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.ViewApdater.AdapterDataNotification
import com.iuh.tranthang.myshool.ViewApdater.RecycleViewNotificationAdapter
import com.iuh.tranthang.myshool.model.mNotification
import kotlinx.android.synthetic.main.activity_list_notification.*
import java.util.*

class ListNotificationActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
        dbConnect.sendListNotification {


    lateinit var db: dbConnect
    private lateinit var dbFireStore: FirebaseFirestore
    private lateinit var mAuth: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_notification)
        // Tắt menu
        val actionBar = supportActionBar
        actionBar!!.hide()

        db = dbConnect(this)
        db.getListNotification()
        swipe_notification_main.setOnRefreshListener { onRefresh() }
    }

    /**
     * Nhận dữ liệu từ db trả về
     * Lấy danh sách Các tin nhắn mẫu
     */
    fun getListNotification_Template(list: ArrayList<mNotification>) {
        cyc_notifi.layoutManager = LinearLayoutManager(this)
        if (list.size > 0) {
            val simpleAdapter = RecycleViewNotificationAdapter(list, { partItem: mNotification ->
                partItemClicked(partItem)
            })
            var adap = AdapterDataNotification(applicationContext, list)
            cyc_notifi.adapter = simpleAdapter
            adap.notifyDataSetChanged()
            swipe_notification_main.setRefreshing(false)
        }
    }

    /**
     * Sự kiện click từ Recycle View
     */
    private fun partItemClicked(partItem: mNotification) {

    }

    /**
     * Xử lý sự kiện khi refresh
     * */
    override fun onRefresh() {
        swipe_notification_main.setRefreshing(true)
        db.getListNotification()
    }

    /**
     * Nhập thông tin trả về
     */
    override fun getList(list: ArrayList<mNotification>) {
        getListNotification_Template(list)
    }
}
