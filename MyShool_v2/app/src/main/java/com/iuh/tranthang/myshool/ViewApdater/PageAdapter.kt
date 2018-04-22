package com.iuh.tranthang.myshool.ViewApdater

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by ThinkPad on 4/7/2018.
 */
class PageAdapter(fm:FragmentManager): FragmentPagerAdapter(fm) {

    var mFm = fm
    var mFragmentItems:ArrayList<Fragment> = ArrayList()
    var mFragmentTitle: ArrayList<String> = ArrayList()
    fun addFragment(fragmentItem: Fragment, fragmentTitle:String){
        mFragmentItems.add(fragmentItem)
        mFragmentTitle.add(fragmentTitle)
    }
    override fun getItem(position: Int): Fragment {
        return mFragmentItems[position]
    }

    override fun getCount(): Int {
        return mFragmentItems.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitle[position]
    }
}