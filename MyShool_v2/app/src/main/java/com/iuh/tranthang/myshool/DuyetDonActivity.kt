package com.iuh.tranthang.myshool

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.ViewApdater.AdapterDataTakeLeaves
import com.iuh.tranthang.myshool.ViewApdater.RecycleViewXetDonAdaptervar
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.mTakeLeave
import com.iuh.tranthang.myshool.model.mUser

class DuyetDonActivity : AppCompatActivity(), dbConnect.inforUserLogin, dbConnect.returnList,
        SwipeRefreshLayout.OnRefreshListener {

    private lateinit var db: dbConnect
    private lateinit var mUser: mUser

    private lateinit var ryr: RecyclerView
    private lateinit var ref: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_duyet_don)
        db = dbConnect(this)
        db.getUser()

        val actionBar = supportActionBar
        actionBar!!.hide()

        ryr = findViewById(R.id.cyc_xinphep)
        ref = findViewById(R.id.swipe_xinphep_main)
        ref.setOnRefreshListener { onRefresh() }
    }

    override fun getInfoUser(mU: mUser) {
        mUser = mU
        if (mUser.getPermission().compareTo("1") == 0) {
            if (mUser.getChucVu().compareTo("Tổ trưởng") == 0) {
                Log.e("tmtt", "listt ahihihi orrrrrr")
                if (mUser.getToCongTac().equals("Tổ Toán")) {
                    db.getDonXin("11")
                }
                if (mUser.getToCongTac().equals("Tổ Lý")) {
                    db.getDonXin("12")
                }
                if (mUser.getToCongTac().equals("Tổ Hóa")) {
                    db.getDonXin("13")
                }
            }
        }
    }

    fun loadList(list: ArrayList<mTakeLeave>) {
        ryr.layoutManager = LinearLayoutManager(this)
        var listTemp: ArrayList<mTakeLeave> = ArrayList<mTakeLeave>()
        if (list.size > 0) {
            for (item in list) {
                if (item.status.compareTo("1") == 0) {
                    listTemp.add(item)
                }
            }
        }
        if (listTemp.size > 0) {
            val simple = RecycleViewXetDonAdaptervar(listTemp, { mTake: mTakeLeave ->
                Log.e("tmt", "simple" + mTake.fullname)

                var intent: Intent = Intent(this, DuyenDonApplyActivity::class.java)
                intent.putExtra(Parameter.takeLeave_id, mTake.id)
                intent.putExtra(Parameter.takeLeave_title, mTake.fullname)
                intent.putExtra(Parameter.takeLeave_content, mTake.content)
                intent.putExtra(Parameter.takeLeave_date, mTake.timeStart + " - " + mTake.timeEnd)
                ContextCompat.startActivity(this, intent, null)
            })
            var adap = AdapterDataTakeLeaves(applicationContext, listTemp)
            ryr.adapter = simple
            adap.notifyDataSetChanged()
            ref.setRefreshing(false)
        }
    }

    override fun listTakeLeaves(mList: ArrayList<mTakeLeave>) {
        loadList(mList)
    }

    override fun onRefresh() {
        ref.setRefreshing(true)
        if (mUser.getPermission().compareTo("1") == 0) {
            if (mUser.getChucVu().compareTo("Tổ trưởng") == 0) {
                Log.e("tmtt", "listt ahihihi orrrrrr")
                if (mUser.getToCongTac().equals("Tổ Toán")) {
                    db.getDonXin("11")
                }
                if (mUser.getToCongTac().equals("Tổ Lý")) {
                    db.getDonXin("12")
                }
                if (mUser.getToCongTac().equals("Tổ Hóa")) {
                    db.getDonXin("13")
                }
            }
        }
    }

}
