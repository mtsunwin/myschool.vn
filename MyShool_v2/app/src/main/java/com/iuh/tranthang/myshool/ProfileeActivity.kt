package com.iuh.tranthang.myshool

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import com.iuh.tranthang.myshool.R.id.tableLayout


/**
 * Created by Administrator on 4/16/2018.
 */
class ProfileeActivity: AppCompatActivity(){

    var pagerAdapter : PageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(tableLayout)


    }
}