package com.iuh.tranthang.myshool.ViewApdater

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.mNotification
import kotlinx.android.synthetic.main.layout_item_list_notification.view.*

/**
 * Created by ThinkPad on 5/5/2018.
 */
class RecycleViewNotificationAdapter(var items: ArrayList<mNotification>)
    : RecyclerView.Adapter<RecycleViewNotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(parent!!)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.bind(
                items[position].title,
                items[position].title,
                items[position].content,
                ""
        )
    }

    class ViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_list_notification,
            parent, false)) {
        fun bind(title: String, name: String, chucvu: String, phone: String) = with(itemView) {
            txtList_tile_notification.text = title
        }
    }
}