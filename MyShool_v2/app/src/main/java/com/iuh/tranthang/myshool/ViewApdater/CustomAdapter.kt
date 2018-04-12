package com.iuh.tranthang.myshool.ViewApdater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.adm_display

/**
 * Created by ThinkPad on 4/7/2018.
 */
class CustomAdapter(var context: Context, var listTitle: ArrayList<adm_display>) : BaseAdapter() {

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

    override fun getView(p0: Int, converview: View?, p2: ViewGroup?): View {
        var view: View
        var viewHolder: ViewHolder
        if (converview == null) {
            var layoutinflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutinflater.inflate(R.layout.layout_item_list_user, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = converview
            viewHolder = converview.tag as ViewHolder
        }

        var item: adm_display = getItem(p0) as adm_display
        viewHolder.text_fullname.text = item.getName()
        viewHolder.img_avatar.setImageResource(item.getImg())
        return view
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