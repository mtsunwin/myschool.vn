package com.iuh.tranthang.myshool.ViewApdater

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.mTakeLeave
import kotlinx.android.synthetic.main.layout_item_list_notification.view.*

/**
 * Created by ThinkPad on 5/15/2018.
 */

class RecycleViewXetDonAdaptervar(var items: ArrayList<mTakeLeave>,
                                  val clickListener: (mTakeLeave) -> Unit)
    : RecyclerView.Adapter<RecycleViewTakeLeavesAdapter.rycViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecycleViewTakeLeavesAdapter.rycViewHolder {
        return RecycleViewTakeLeavesAdapter.rycViewHolder(parent!!)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecycleViewTakeLeavesAdapter.rycViewHolder?, position: Int) {
        holder!!.bind(items[position], clickListener)
    }

    class rycViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_list_notification,
            parent, false)) {
        fun bind(item: mTakeLeave,
                 clickListener: (mTakeLeave) -> Unit) = with(itemView) {
            txtList_tile_notification.text = "Đơn xin nghỉ phép"

            var strTemp: String = ""
            strTemp += "<b>" + item.fullname + "</b> với lý do:<br>"
            strTemp += item.content + "<br>"
            strTemp += "<b>Từ</b> " + item.timeStart + " <b>đến</b> " + item.timeEnd

            txtList_content_notification.text = Html.fromHtml(strTemp)
            img_group.visibility = View.GONE
            ic_eye.visibility = View.GONE
            txtList_count_notification.visibility = View.GONE
            txtList_time_notification.visibility = View.GONE
            ic_calendar_1.visibility = View.GONE

            var str = ""
            when (item.status.toInt()) {
                1 -> {
                    card_view.setBackgroundColor(resources.getColor(R.color.colorAccentOpacity))
                    str += "Chờ duyệt"
                }
                2 -> {
                    str += "Chấp nhận"
                }
                3 -> {
                    str += "Từ chối"
                }
            }
            txtList_group_notification.text = str
            linner_card.setOnClickListener { clickListener(item) }
        }
    }
}