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
class RecycleViewNotificationAdapter(var items: ArrayList<mNotification>, val clickListener: (mNotification) -> Unit)
    : RecyclerView.Adapter<RecycleViewNotificationAdapter.rycViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): rycViewHolder {
        return rycViewHolder(parent!!)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: rycViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
    }

    class rycViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_list_notification,
            parent, false)) {
        fun bind(item: mNotification,
                 clickListener: (mNotification) -> Unit) = with(itemView) {
            txtList_tile_notification.text = item.title
            txtList_content_notification.text = item.content
            txtList_count_notification.text = item.count
            val listStringPermission = resources.getStringArray(R.array.select_notification_to_send)
            txtList_group_notification.text = listStringPermission[item.group.toInt()]
            txtList_time_notification.text = item.dateTime.substring(0, 10)
            linner_card.setOnClickListener { clickListener(item) }
        }
    }
}