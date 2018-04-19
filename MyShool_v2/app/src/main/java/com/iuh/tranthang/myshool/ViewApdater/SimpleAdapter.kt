package com.iuh.tranthang.myshool.ViewApdater

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iuh.tranthang.myshool.R
import kotlinx.android.synthetic.main.layout_item_list_user.view.*

/**
 * Created by ThinkPad on 4/19/2018.
 */

class SimpleAdapter(private val items: ArrayList<String>) : RecyclerView.Adapter<SimpleAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addItem(name: String) {
        items.add(name)
        notifyItemInserted(items.size)
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item_list_user, parent, false)) {

        fun bind(name: String) = with(itemView) {
            txt_fullname.text = name
        }
    }
}
