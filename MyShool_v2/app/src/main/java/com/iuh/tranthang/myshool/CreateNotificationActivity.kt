package com.iuh.tranthang.myshool

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.iuh.tranthang.myshool.Firebase.NotificationUtils
import kotlinx.android.synthetic.main.activity_create_notification.*

class CreateNotificationActivity : AppCompatActivity() {
    var notification: NotificationUtils? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_notification)
        notification = NotificationUtils(applicationContext)

        btnCreatedNotification.setOnClickListener { view ->
            Log.e("tmt", "Thang dep trai da click vao day")
            runn()

        }
    }

    private fun runn(): Boolean {
        val message = "Test Notification"

        val resultIntent = Intent(applicationContext, AdminActivity::class.java)
        resultIntent.putExtra("message", message)
        notification!!.showNotificationMessage("Thang dep trai", message, "", resultIntent)
        return true
    }
}
