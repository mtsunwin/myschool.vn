package com.iuh.tranthang.myshool.ViewApdater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.daimajia.swipe.interfaces.SwipeAdapterInterface
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.User

/**
 * Created by ThinkPad on 4/7/2018.
 */
class CustomAdapter(var context: Context, var listTitle: ArrayList<User>) : BaseAdapter(), SwipeAdapterInterface {

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_layout
    }

    class ViewHolder(row: View) {
        var text_fullname: TextView
        var text_chucvu: TextView
        var img_avatar: ImageView
        var btn_call: Button
//        var swipe_item: SwipeLayout

        init {
            text_fullname = row.findViewById(R.id.txt_fullname)
            text_chucvu = row.findViewById(R.id.txt_chucvu)
            img_avatar = row.findViewById(R.id.img_avatar)
            btn_call = row.findViewById(R.id.btn_Call)
//            swipe_item = row.findViewById(R.id.swipe_container)
//            swipe_item.addDrag(SwipeLayout.DragEdge.Right, swipe_item.findViewById(R.id.btn_Call))
        }

    }


    override fun getView(position: Int, converview: View?, p2: ViewGroup?): View {
        var view: View
        var viewHolder: ViewHolder
        if (converview == null) {
            var layoutinflater = LayoutInflater.from(context)
            view = layoutinflater.inflate(R.layout.layout_item_list_user, p2, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = converview
            viewHolder = converview.tag as ViewHolder
        }

        var item = listTitle[position]
        viewHolder.text_fullname.text = item.getFullname().trim()
//        viewHolder.swipe_item.set
        val listStringPermission = context.resources.getStringArray(R.array.select_permission)

        when (item.getPermission()) {
            "0" -> viewHolder.text_chucvu.text = listStringPermission!!.get(0)
            "1" -> viewHolder.text_chucvu.text = listStringPermission!!.get(1)
            "2" -> viewHolder.text_chucvu.text = listStringPermission!!.get(2)
            "3" -> viewHolder.text_chucvu.text = listStringPermission!!.get(3)
            else -> viewHolder.text_chucvu.text = ""
        }
        if (item.getNumberphone() != null) {
            viewHolder.btn_call.visibility = view.visibility
        }
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