package com.iuh.tranthang.myshool.ViewApdater

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.User

/**
 * Created by ThinkPad on 4/7/2018.
 */
class CustomAdapter(private var activity: Activity, private var listTitle: ArrayList<User>) : BaseAdapter() {

    class ViewHolder(row: View) {
        var text_fullname: TextView
        var text_chucvu: TextView
        var img_avatar: ImageView
        var btn_call: Button

        init {
            text_fullname = row.findViewById(R.id.txt_fullname)
            text_chucvu = row.findViewById(R.id.txt_chucvu)
            img_avatar = row.findViewById(R.id.img_avatar)
            btn_call = row.findViewById(R.id.btn_Call)
        }

    }

    override fun getView(position: Int, converview: View?, p2: ViewGroup?): View {
        var view: View
        var viewHolder: ViewHolder
        if (converview == null) {
            var layoutinflater: LayoutInflater = LayoutInflater.from(activity)
            view = layoutinflater.inflate(R.layout.layout_item_list_user, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = converview
            viewHolder = converview.tag as ViewHolder
        }

        var item = listTitle[position]
        viewHolder.text_fullname.text = item.getFullname()
        viewHolder.img_avatar.setImageResource(R.drawable.team_group)

        return view as View
    }

    override fun getItem(position: Int): Any {
        return listTitle.get(position)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return listTitle.size
    }

}