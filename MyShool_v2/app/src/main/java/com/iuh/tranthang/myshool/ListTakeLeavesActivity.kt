package com.iuh.tranthang.myshool

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.ViewApdater.AdapterDataTakeLeaves
import com.iuh.tranthang.myshool.ViewApdater.RecycleViewTakeLeavesAdapter
import com.iuh.tranthang.myshool.model.mTakeLeave

class ListTakeLeavesActivity : AppCompatActivity(),
        SwipeRefreshLayout.OnRefreshListener,
        dbConnect.returnList {

    private lateinit var ryr: RecyclerView
    private lateinit var db: dbConnect
    private lateinit var ref: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_take_leaves)

        val actionBar = supportActionBar
        actionBar!!.hide()

        db = dbConnect(this)

        ryr = findViewById(R.id.cyc_takeleaves)
        ref = findViewById(R.id.swipe_takeleaves_main)
        ref.setOnRefreshListener { onRefresh() }
        db.getListTakeLeaves()
    }

    fun loadList(list: ArrayList<mTakeLeave>) {
        ryr.layoutManager = LinearLayoutManager(this)
        if (list.size > 0) {
            val simple = RecycleViewTakeLeavesAdapter(list, { mTake: mTakeLeave ->
                Log.e("tmt", "simple")
            })
            var adap = AdapterDataTakeLeaves(applicationContext, list)
            ryr.adapter = simple
            adap.notifyDataSetChanged()
            ref.setRefreshing(false)
        }
    }

    override fun onRefresh() {
        ref.setRefreshing(true)
        db.getListTakeLeaves()
    }

    override fun listTakeLeaves(mList: java.util.ArrayList<mTakeLeave>) {
        loadList(mList)
    }


}
