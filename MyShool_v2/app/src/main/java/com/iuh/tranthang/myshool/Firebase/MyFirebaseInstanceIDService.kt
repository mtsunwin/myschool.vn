package com.iuh.tranthang.myshool.Firebase

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


/**
 * Created by ThinkPad on 4/21/2018.
 */
class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    private val TAG = "tmt ID Service"

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

        storeRegIdInPref(refreshedToken)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken)

        val registrationComplete: Intent = Intent("registrationComplete")
        registrationComplete.putExtra("token", refreshedToken)
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete)
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        Log.e(TAG, "sendRegistrationToServer: $token")
    }

    private fun storeRegIdInPref(token: String) {
        val pref = applicationContext.getSharedPreferences("ah_firebase", 0)
        val editor = pref.edit()
        editor.putString("regId", token)
        editor.commit()
    }
}