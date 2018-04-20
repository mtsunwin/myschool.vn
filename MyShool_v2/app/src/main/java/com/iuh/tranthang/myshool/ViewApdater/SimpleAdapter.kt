package com.iuh.tranthang.myshool.ViewApdater

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.User
import kotlinx.android.synthetic.main.layout_item_list_user.view.*

/**
 * Created by ThinkPad on 4/19/2018.
 */

class SimpleAdapter(private val items: ArrayList<User>) : RecyclerView.Adapter<SimpleAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position].getFullname(), items[position].getPermission(), items[position].getNumberphone())
    }

    override fun getItemCount(): Int = items.size

    fun addItem(name: String) {
//        items.add(name)
        notifyItemInserted(items.size)
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item_list_user, parent, false)) {

        fun bind(name: String, chucvu: String, phone: String) = with(itemView) {
            val listStringPermission = context.resources.getStringArray(R.array.select_permission)
            txt_fullname.text = name
            txt_chucvu.text = chucvu
            when (chucvu) {
                "0" -> txt_chucvu.text = ""
                "1" -> txt_chucvu.text = listStringPermission!!.get(1)
                "2" -> txt_chucvu.text = listStringPermission!!.get(2)
                "3" -> txt_chucvu.text = listStringPermission!!.get(3)
                else -> txt_chucvu.text = listStringPermission!!.get(4)
            }
            if (phone.length > 0) {
                btn_Call.visibility = visibility
            }
        }
    }
}
