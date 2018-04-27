package com.iuh.tranthang.myshool.Firebase

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.iuh.tranthang.myshool.AdminActivity
import com.iuh.tranthang.myshool.R
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by ThinkPad on 4/21/2018.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var notificationUtils: NotificationUtils? = null

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

    private fun handleDataMessage(json: JSONObject) {
        try {
            val data = json.getJSONObject("data")

            val title = data.getString("title")
            val message = data.getString("message")
            val isBackground = data.getBoolean("is_background")
            val imageUrl = data.getString("image")
            val timestamp = data.getString("timestamp")
            val payload = data.getJSONObject("payload")

            Log.e("tmt", "title: $title")
            Log.e("tmt", "message: $message")
            Log.e("tmt", "isBackground: $isBackground")
            Log.e("tmt", "payload: " + payload.toString())
            Log.e("tmt", "imageUrl: $imageUrl")
            Log.e("tmt", "timestamp: $timestamp")
            val resultIntent = Intent(applicationContext, AdminActivity::class.java)
            resultIntent.putExtra("message", message)
            showNotificationMessage(applicationContext, title, message, timestamp, resultIntent)
            if (!notificationUtils!!.isAppIsInBackground(applicationContext)) {
                // app is in foreground, broadcast the push message
                val pushNotification = Intent("pushNotification")
                pushNotification.putExtra("message", message)
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

                // play notification sound
                val notificationUtils = NotificationUtils(applicationContext)
//                notificationUtils.playNotificationSound()
            } else {
                // app is in background, show the notification in notification tray
                //                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                //                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    Log.e("tmt", "Show this")

                    showNotificationMessage(applicationContext, title, message, timestamp, resultIntent)
                } else {
                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(applicationContext, title, message, timestamp, resultIntent, imageUrl)
                }
            }
        } catch (e: JSONException) {
            Log.e("tmt", "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e("tmt", "Exception: " + e.message)
        }

    }

    private fun showNotificationMessage(context: Context, title: String, message: String, timeStamp: String, intent: Intent) {
        notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils!!.showNotificationMessage(title, message, timeStamp, intent)
    }
}