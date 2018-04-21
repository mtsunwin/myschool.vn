package com.iuh.tranthang.myshool.Firebase

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.iuh.tranthang.myshool.AdminActivity
import com.iuh.tranthang.myshool.R

/**
 * Created by ThinkPad on 4/21/2018.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        showNotification(p0!!.getData().get("message"))
    }

    private fun showNotification(message: String?) {
        var i: Intent = Intent(this, AdminActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        var pendingIntent: PendingIntent = PendingIntent.getActivity(this,
                0, i, PendingIntent.FLAG_CANCEL_CURRENT)
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("oke baby")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_house)
                .setContentIntent(pendingIntent)

        var manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }
}