package com.iuh.tranthang.myshool.model

/**
 * Created by ThinkPad on 5/5/2018.
 */
data class mNotification(val id: String, val title: String, val content: String, val group: String) {
    lateinit var count: String
    lateinit var dateTime: String
    lateinit var listView: ArrayList<mNotificationUser>
}