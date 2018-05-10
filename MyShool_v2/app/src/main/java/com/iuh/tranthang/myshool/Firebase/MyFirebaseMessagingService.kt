package com.iuh.tranthang.myshool.Firebase

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.iuh.tranthang.myshool.NotificationPanActivity
import com.iuh.tranthang.myshool.model.Parameter
import org.json.JSONException
import org.json.JSONObject


/**
 * Created by ThinkPad on 4/21/2018.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var notificationUtils: NotificationUtils? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
//        showNotification(remoteMessage!!.getData().get("message"))

        if (remoteMessage == null)
            return

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.e("tmt", "Notification Body: " + remoteMessage.notification!!.body!!)
            handleNotification(remoteMessage.notification!!.body)
        }
        Log.e("onMessageReceived tmt", "Data Payload: " + remoteMessage.data.toString())
        if (remoteMessage.data.size > 0) {
            Log.e("tmt", "Data Payload: " + remoteMessage.data.toString())
            try {
                val json = JSONObject(remoteMessage.data.toString())
                handleDataMessage(json)
            } catch (e: Exception) {
                Log.e("tmt", "Exception: " + e.message)
            }

        }
    }

    private fun handleNotification(message: String?) {
        if (!NotificationUtils(applicationContext).isAppIsInBackground()) {
            // app is in foreground, broadcast the push message
            val pushNotification = Intent("pushNotification")
            pushNotification.putExtra("message", message)
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
        }
    }

    private fun handleDataMessage(json: JSONObject) {
        try {
            val data = json.getJSONObject("data")
            val title = xulyChuoi(data.getString("title"))
            val message = xulyChuoi(data.getString("message"))
            val isBackground = data.getBoolean("is_background")
            val imageUrl = data.getString("image")
            val timestamp = data.getString("timestamp")
            val payload = data.getJSONObject("payload")
            val resultIntent = Intent(applicationContext, NotificationPanActivity::class.java)
            resultIntent.putExtra(Parameter.KEY_CONTENT_MESS, message)
            resultIntent.putExtra(Parameter.KEY_ID_MESS, imageUrl)
            resultIntent.putExtra(Parameter.KEY_Title_MESS, title)
            showNotificationMessage(applicationContext, title, message, timestamp, resultIntent)

            if (!notificationUtils!!.isAppIsInBackground()) {
                // app is in foreground, broadcast the push message
                val pushNotification = Intent("pushNotification")
                pushNotification.putExtra("message", message)
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
            } else {
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(applicationContext, title, message, timestamp, resultIntent)
                }
            }
        } catch (e: JSONException) {
            Log.e("tmt", "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e("tmt", "Exception: " + e.message)
        }
    }

    private fun showNotificationMessage(context: Context, title: String, message: String,
                                        timeStamp: String, intent: Intent) {
        notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils!!.showNotificationMessage(title, message, timeStamp, intent)
    }

    private fun xulyChuoi(str: String): String {
        return str.replace(".-.", " ")
    }
}