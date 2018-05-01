package com.iuh.tranthang.myshool.ViewApdater

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.User

/**
 * Created by ThinkPad on 4/19/2018.
 */
internal class DataAdapter(private val names: ArrayList<User>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DataAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_item_list_user, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.tv_names.text = names[i].getFullname()
    }
    override fun getItemCount(): Int {
        return names.size
    }

    fun addItem(country: String) {
//        names.add(country)
        notifyItemInserted(names.size)
    }

    fun removeItem(position: Int) {
        names.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, names.size)
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_names: TextView

        init {
            tv_names = view.findViewById<TextView>(R.id.txt_fullname) as TextView

        }
    }
}