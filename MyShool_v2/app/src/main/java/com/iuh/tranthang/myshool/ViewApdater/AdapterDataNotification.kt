package com.iuh.tranthang.myshool.ViewApdater

import android.content.Context
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.daimajia.swipe.interfaces.SwipeAdapterInterface
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.mNotification

/**
 * Created by ThinkPad on 5/6/2018.
 */
class AdapterDataNotification(var context: Context, var listTitle: ArrayList<mNotification>)
    : BaseAdapter(), SwipeAdapterInterface {

    override fun getView(position: Int, converview: View, p2: ViewGroup?): View {
        var view: View
        var viewHolder: vwHolder
        if (converview != null) {
            var layoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.layout_item_list_notification, p2, false)
            viewHolder = vwHolder(view)
            view.tag = viewHolder
        } else {
            view = converview
            viewHolder = converview!!.tag as vwHolder
        }
        var item = listTitle[position]
        viewHolder.title.text = item.title.trim()
        viewHolder.content.text = item.content.trim()
        viewHolder.date.text = item.dateTime.trim()
        viewHolder.content.text = item.content.trim()
        viewHolder.group.text = item.group.trim()
        return view
    }

    override fun getItem(p0: Int): Any {
        return listTitle.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return listTitle.size
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_notification
    }

    class vwHolder(row: View) {
        var title: TextView
        var content: TextView
        var group: TextView
        var count: TextView
        var date: TextView
        lateinit var card: CardView

        init {
            title = row.findViewById(R.id.txtList_tile_notification)
            content = row.findViewById(R.id.txtList_content_notification)
            group = row.findViewById(R.id.txtList_group_notification)
            count = row.findViewById(R.id.txtList_count_notification)
            date = row.findViewById(R.id.txtList_time_notification)
            card = row.findViewById(R.id.card_view)
        }
    }
}