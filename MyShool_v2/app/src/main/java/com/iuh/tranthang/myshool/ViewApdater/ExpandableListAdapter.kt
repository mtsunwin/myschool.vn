package com.iuh.tranthang.myshool.ViewApdater

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.RegisterActivity
import com.iuh.tranthang.myshool.model.adm_display

/**
 * Created by ThinkPad on 4/8/2018.
 */
class ExpandableListAdapter(val context: Context, val listOfHeaderData: ArrayList<adm_display>,
                            val listofChildData: HashMap<String, ArrayList<adm_display>>) : BaseExpandableListAdapter() {

    override fun getGroup(position: Int): Any {
        return listOfHeaderData[position]
    }

    override fun isChildSelectable(headerPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, converView: View?, parent: ViewGroup?): View {
//        val headerTitle = getGroup(groupPosition) as String
        val headerTitle = getGroup(groupPosition) as adm_display

        val view: View = LayoutInflater.from(context).inflate(R.layout.layout_list_header, parent, false)
        val listHeaderText = view.findViewById<AppCompatTextView>(R.id.list_header_text) as AppCompatTextView
        val listHeaderImg = view.findViewById<AppCompatTextView>(R.id.list_header_img) as AppCompatImageView
        listHeaderText.setTypeface(null, Typeface.BOLD)
        listHeaderText.text = headerTitle.getName()
        listHeaderImg.setImageResource(headerTitle.getImg())

        return view

    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listofChildData[listOfHeaderData[groupPosition].getName()]!!.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return listofChildData[listOfHeaderData[groupPosition].getName()]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, converView: View?, parent: ViewGroup?): View {
        val childText = getChild(groupPosition, childPosition) as adm_display
        val view: View = LayoutInflater.from(context).inflate(R.layout.layout_list_item, parent, false)

        val listItemText = view.findViewById<AppCompatTextView>(R.id.list_item_list) as AppCompatTextView
        listItemText.text = childText.getName()

        val listItemImg = view.findViewById<AppCompatImageView>(R.id.list_item_img) as AppCompatImageView
        listItemImg.setImageResource(childText.getImg())

        listItemImg.setOnClickListener(View.OnClickListener {
            when (childText.getId()) {
                12 -> addAccount(converView)
            }
        })

        listItemText.setOnClickListener(View.OnClickListener {
            Log.e("tmt", childText.getName() + "")
        })

        return view
    }

    // ID 12
    private fun addAccount(converView: View?) {
        var intent: Intent = Intent(converView!!.context, RegisterActivity::class.java)
        ContextCompat.startActivity(context, intent, null)
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGroupCount(): Int {
        return listOfHeaderData.size
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}