package com.iuh.tranthang.myshool.ViewApdater

import android.content.Context
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.daimajia.swipe.interfaces.SwipeAdapterInterface
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.mTakeLeave

/**
 * Created by ThinkPad on 5/13/2018.
 */
class AdapterDataTakeLeaves(var context: Context, var listTakesLeaves: ArrayList<mTakeLeave>)
    : BaseAdapter(), SwipeAdapterInterface {
    override fun getView(position: Int, converview: View, p2: ViewGroup?): View {
        var view: View
        var vHolder: vwmHolder

        if (converview != null) {
            var layoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.layout_item_list_notification, p2, false)
            vHolder = vwmHolder(view)
            view.tag = vHolder
        } else {
            view = converview
            vHolder = converview!!.tag as vwmHolder
        }
        var item = listTakesLeaves[position]
        vHolder.title.text = "Đơn xin nghỉ"
        var str = item.content

        vHolder.content.text = item.content
        return view
    }

    override fun getItem(p0: Int): Any {
        return listTakesLeaves.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return listTakesLeaves.size
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_takeleaves_main
    }

    class vwmHolder(row: View) {
        var title: TextView
        var content: TextView
        var group: TextView
        var count: TextView
        var date: TextView
        var card: CardView

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