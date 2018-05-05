package com.iuh.tranthang.myshool.ViewApdater

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.mNotification
import kotlinx.android.synthetic.main.layout_item_list_user.view.*

/**
 * Created by ThinkPad on 5/5/2018.
 */
class RecycleViewNotificationAdapter(private val items: ArrayList<mNotification>)
    : RecyclerView.Adapter<RecycleViewNotificationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(parent!!)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.bind(
                items[position].title,
                items[position].content,
//                items[position].nu())
                ""
        )
    }

    class ViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_list_notification,
            parent, false)) {
        fun bind(name: String, chucvu: String, phone: String) = with(itemView) {
//            val listStringPermission = context.resources.getStringArray(R.array.select_permission)
//            txt_fullname.text = name
//            txt_chucvu.text = chucvu
//            when (chucvu) {
//                "0" -> txt_chucvu.text = "Kế toán"
//                "1" -> txt_chucvu.text = "Giáo viên"
//                "2" -> txt_chucvu.text = "Nhân viên"
//                "3" -> txt_chucvu.text = "Admin"
//                else -> txt_chucvu.text = ""
//            }
//            if (phone.length > 0) {
//                btn_Call.visibility = visibility
//            }
        }
    }
}