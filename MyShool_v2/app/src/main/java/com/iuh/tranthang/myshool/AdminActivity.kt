package com.iuh.tranthang.myshool

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.iuh.tranthang.myshool.Firebase.NotificationUtils
import com.iuh.tranthang.myshool.ViewApdater.ExpandableListAdapter
import com.iuh.tranthang.myshool.model.adm_display
import kotlinx.android.synthetic.main.activity_admin.*


class AdminActivity : AppCompatActivity() {

    private var drawerLayout: DrawerLayout? = null
    private var abdt: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null

    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        var token = getSharedPreferences("username", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val intent = Intent(this, InsideActivity::class.java)
        val intent_profile = Intent(this, ProfileActivity::class.java)

        val listHeader: ArrayList<adm_display> = ArrayList()
        listHeader.add(adm_display("Thông tin nhân viên", R.drawable.team_group, 1))
        listHeader.add(adm_display("Quản lý thông báo", R.drawable.team_group, 2))

        val inforStaff: ArrayList<adm_display> = ArrayList()
        inforStaff.add(adm_display("Danh sách tài khoản", R.drawable.team_group, 11))
        inforStaff.add(adm_display("Thêm tài khoản", R.drawable.team_group, 12))
        inforStaff.add(adm_display("Xóa nhân viên", R.drawable.team_group, 13))

        val inforNotify: ArrayList<adm_display> = ArrayList()
        inforNotify.add(adm_display("Danh sách thông báo", R.drawable.team_group, 21))
        inforNotify.add(adm_display("Tạo thông báo", R.drawable.team_group, 22))

        val listChild = HashMap<String, ArrayList<adm_display>>()

        listChild.put(listHeader[0].getName(), inforStaff)
        listChild.put(listHeader[1].getName(), inforNotify)

        val expandableListAdapter = ExpandableListAdapter(this, listHeader, listChild)

        expandable_list_view.setAdapter(expandableListAdapter)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.menuNavigation)
        abdt = ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close)

        val drawerIndicatorEnabled = abdt!!.isDrawerIndicatorEnabled
        drawerLayout!!.addDrawerListener(abdt!!)
        abdt!!.syncState()
        navigationView!!.setNavigationItemSelectedListener(
                object : NavigationView.OnNavigationItemSelectedListener {
                    override fun onNavigationItemSelected(item: MenuItem): Boolean {
                        var boolean: Boolean?
                        if (item!!.itemId == R.id.DangXuat) {
                            var token = getSharedPreferences("username", Context.MODE_PRIVATE)
                            var editor = token.edit()
                            editor.putString("loginusername", " ")
                            editor.commit()
                            var token_ps = getSharedPreferences("permission", Context.MODE_PRIVATE)
                            var editor_ps = token_ps.edit()
                            editor_ps.putString("permission", " ")
                            editor_ps.commit()
                            startActivity(intent)
                            finish()
                            boolean = true
                        } else if (item!!.itemId == R.id.itemTrangCaNhan) {
                            startActivity(intent_profile)
                            boolean = true
                        } else {
                            boolean = false
                        }

                        return boolean!!
                    }
                }
        )
        // BroadcastReceiver
        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // checking for type intent filter
                if (intent.action == "registrationComplete") {
                    Log.e("tmt", "registrationComplete")
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic("global")
                    displayFirebaseRegId()
                } else if (intent.action == "pushNotification") {
                    // new push notification is received
                    val message = intent.getStringExtra("message")
                    Toast.makeText(applicationContext, "Push notification: $message", Toast.LENGTH_LONG).show()
                }
            }
        }
        displayFirebaseRegId()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var boolean: Boolean?
        if (item!!.itemId == R.id.DangXuat) {
            var token = getSharedPreferences("username", Context.MODE_PRIVATE)
            var editor = token.edit()
            editor.putString("loginusername", " ")
            editor.commit()
            var intent = Intent(this, InsideActivity::class.java)
            startActivity(intent)
            finish()
            boolean = true
        } else if (item!!.itemId == R.id.itemTrangCaNhan) {
            val intent_profile = Intent(this, ProfileActivity::class.java)
            startActivity(intent_profile)
            boolean = true
        } else {
            boolean = super.onOptionsItemSelected(item)
        }
        return boolean!!
    }

    private fun displayFirebaseRegId() {
        val pref = applicationContext.getSharedPreferences("ah_firebase", 0)
        val regId = pref.getString("regId", null)

        Log.e("tmt", "Firebase reg id: " + regId!!)

    }


    override fun onResume() {
        super.onResume()

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                IntentFilter("registrationComplete"))

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                IntentFilter("pushNotification"))

        // clear the notification area when the app is opened
        NotificationUtils(applicationContext).clearNotifications(applicationContext)
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver)
        super.onPause()
    }
}
