package com.iuh.tranthang.myshool.ViewApdater

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.mUser
import kotlinx.android.synthetic.main.layout_item_list_user.view.*
import kotlinx.android.synthetic.main.layout_item_list_user_updatesalary.view.*


/**
 * Created by ThinkPad on 4/19/2018.
 */

class SimpleAdapter(private val items: ArrayList<mUser>) : RecyclerView.Adapter<SimpleAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position].getUid(), items[position].getFullname(), items[position].getPermission(), items[position].getNumberphone(),items[position].getUrl())
    }

    override fun getItemCount(): Int = items.size

    fun addItem(name: String) {
//        items.add(name)
        notifyItemInserted(items.size)
    }

    fun removeAt(position: Int) {
        Log.e("tmt at", position.toString())
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item_list_user, parent, false)) {

        fun bind(uuid:String, name: String, chucvu: String, phone: String,url:String) = with(itemView) {
            val listStringPermission = context.resources.getStringArray(R.array.select_permission)
            txt_fullname.text = name
            txt_chucvu.text = chucvu
            when (chucvu) {
                "0" -> txt_chucvu.text = "Kế toán"
                "1" -> txt_chucvu.text = "Giáo viên"
                "2" -> txt_chucvu.text = "Nhân viên"
                "3" -> txt_chucvu.text = "Admin"
                else -> txt_chucvu.text = ""
            }
            if (phone.length > 0) {
                btn_Call.visibility = visibility
            }
        }
    }
}
