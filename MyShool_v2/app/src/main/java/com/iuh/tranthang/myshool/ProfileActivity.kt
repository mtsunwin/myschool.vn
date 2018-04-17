package com.iuh.tranthang.myshool

import android.os.Bundle

import android.support.v7.app.AppCompatActivity
import com.iuh.tranthang.myshool.ViewApdater.ActivityFragment
import com.iuh.tranthang.myshool.ViewApdater.PageAdapter
import com.iuh.tranthang.myshool.ViewApdater.ProfileFragment
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    var pageAdapter: PageAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        pageAdapter = PageAdapter(supportFragmentManager)
        pageAdapter!!.addFragment(ProfileFragment(), "PROFILE")
        pageAdapter!!.addFragment(ActivityFragment(), "ACTIVITY")
        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}
