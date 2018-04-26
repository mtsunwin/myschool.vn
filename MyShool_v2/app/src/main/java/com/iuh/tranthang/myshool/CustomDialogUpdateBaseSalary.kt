package com.iuh.tranthang.myshool.ViewApdater

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.User
import kotlinx.android.synthetic.main.layout_item_list_user.view.*


@SuppressLint("ValidFragment")
/**
 * Created by ThinkPad on 4/19/2018.
 */
class CustomDialogUpdateBaseSalary(private val items: ArrayList<User>) : DialogFragment() {

}
