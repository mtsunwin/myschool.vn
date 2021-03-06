package com.iuh.tranthang.myshool

import android.app.Dialog
import android.content.*
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.iuh.tranthang.myshool.Firebase.NotificationUtils
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.ViewApdater.ExpandableListAdapter
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.adm_display
import kotlinx.android.synthetic.main.activity_admin.*
import java.io.File


class AdminActivity : AppCompatActivity() {

    private var drawerLayout: DrawerLayout? = null
    private var abdt: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null

    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null
    private lateinit var dbFireStore: FirebaseFirestore
    private lateinit var mAuth: FirebaseUser

    //bien cho hien thi avatar navigation
    private var view: View? = null
    private var nav_header_imgAvartar: ImageView? = null
    private var nav_header_txtName: TextView? = null
    private var nav_header_txtPermission: TextView? = null
    private var nav_header_layout: LinearLayout? = null
    internal var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var permission: String = ""
    private var name: String = ""
    private var txtURLImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Khởi tạo các đối tượng giao tiếp với firebase
        mAuth = FirebaseAuth.getInstance().currentUser!!
        dbFireStore = FirebaseFirestore.getInstance()
        // Tắt menu
        val actionBar = supportActionBar
        actionBar!!.hide()

        val intent_profile = Intent(this, ProfileActivity::class.java)

        val listHeader: ArrayList<adm_display> = ArrayList()
        listHeader.add(adm_display("Thông tin nhân viên", R.drawable.ic_communication, 1))
        listHeader.add(adm_display("Quản lý thông báo", R.drawable.ic_notification, 2))

        val inforStaff: ArrayList<adm_display> = ArrayList()
        inforStaff.add(adm_display("Danh sách tất cả tài khoản", R.drawable.ic_clipboard, 11))
        inforStaff.add(adm_display("Danh sách Giáo viên", R.drawable.ic_classroom, 991))
        inforStaff.add(adm_display("Danh sách Quản lý", R.drawable.ic_customer_service, 992))
        inforStaff.add(adm_display("Danh sách Kế toán", R.drawable.ic_accounting, 993))
        inforStaff.add(adm_display("Danh sách nhân viên", R.drawable.ic_teacher, 995))
        inforStaff.add(adm_display("Thêm tài khoản", R.drawable.ic_user, 12))
//      inforStaff.add(adm_display("Xóa nhân viên", R.drawable.ic_trash, 13))


        val inforNotify: ArrayList<adm_display> = ArrayList()
        inforNotify.add(adm_display("Danh sách thông báo", R.drawable.ic_list_2, 21))
        inforNotify.add(adm_display("Tạo thông báo", R.drawable.ic_create, 22))

        val listChild = HashMap<String, ArrayList<adm_display>>()

        listChild.put(listHeader[0].getName(), inforStaff)
        listChild.put(listHeader[1].getName(), inforNotify)

        val expandableListAdapter = ExpandableListAdapter(this, listHeader, listChild)

        expandable_list_view.setAdapter(expandableListAdapter)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.menuNavigation)

        // cập nhật avatar navigation
        view = navigationView!!.inflateHeaderView(R.layout.nav_header_main)
        nav_header_layout = findViewById(R.id.nav_header_layout)
        nav_header_imgAvartar = view!!.findViewById(R.id.nav_header_imageView)
        nav_header_txtName = view!!.findViewById(R.id.nav_header_name)
        nav_header_txtPermission = view!!.findViewById(R.id.nav_header_permission)
        nav_header_txtPermission!!.setText(permission.toString())
        nav_header_txtName!!.setText(name.toString())
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        var db = dbConnect(this)
        if (db.isAuthentication()) {
            var dbFireStore = FirebaseFirestore.getInstance()
            dbFireStore!!.collection(Parameter.root_User).document(mAuth!!.uid!!)
                    .get().addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            Log.e("Tmt inside", "mmmmmmmmmmmmmm")
                            var result: DocumentSnapshot = task.result
                            if (result.exists()) {
                                name = result.data[Parameter.comp_fullname].toString()
                                when (result.data[Parameter.comp_Permission].toString()) {
                                    "0" -> {
                                        permission = "Kế toán"
                                    }
                                    "1" -> {
                                        permission = "Giáo viên"
                                    }
                                    "2" -> {
                                        permission = "Nhân viên"
                                    }
                                    "3" -> {
                                        permission = "Admin"
                                    }
                                }
                                nav_header_txtName!!.setText(name.toString())
                                nav_header_txtPermission!!.setText(permission.toString())
                                txtURLImage = result.data[Parameter.comp_url].toString()
                                Log.e("URL:", txtURLImage.toString())
                                if (txtURLImage!!.length > 0) {
                                    try {
                                        val tmpFile = File.createTempFile("img", "png")
                                        val reference = FirebaseStorage.getInstance().getReference("images/")
                                        //  "id" is name of the image file....
                                        reference.child(txtURLImage.toString()).getFile(tmpFile).addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                                            val image = BitmapFactory.decodeFile(tmpFile.getAbsolutePath())
                                            nav_header_imgAvartar!!.setImageBitmap(image)
                                        })
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            } else {
                                Log.e("tmt false", "false")
                            }
                        }
                    })
        }

        //// ket thuc cap nhat avatar/////
        abdt = ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close)

//        val drawerIndicatorEnabled = abdt!!.isDrawerIndicatorEnabled
        drawerLayout!!.addDrawerListener(abdt!!)
        abdt!!.syncState()
        navigationView!!.setNavigationItemSelectedListener(
                object : NavigationView.OnNavigationItemSelectedListener {
                    override fun onNavigationItemSelected(item: MenuItem): Boolean {
                        var boolean: Boolean? = false
                        if (item!!.itemId == R.id.DangXuat) {
                            Log.e("Dang xuat ne", "abc")
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this@AdminActivity)
                            var inflater: LayoutInflater = layoutInflater
                            var view: View = inflater.inflate(R.layout.layout_dialog, null)
                            var content: TextView = view.findViewById<View>(R.id.txtDialog_content) as TextView
                            content.setText("Bạn có muốn đăng xuất")
                            builder.setView(view)
                            builder.setNegativeButton(R.string.dialogAsk_no, object : DialogInterface.OnClickListener { // cancel
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    p0!!.dismiss()

                                }
                            })
                            builder.setPositiveButton(R.string.dialogAsk_yes, object : DialogInterface.OnClickListener { // apply
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    var token = getSharedPreferences("username", Context.MODE_PRIVATE)
                                    var editor = token.edit()
                                    editor.putString("loginusername", " ")
                                    editor.commit()
                                    var token_ps = getSharedPreferences("permission", Context.MODE_PRIVATE)
                                    var editor_ps = token_ps.edit()
                                    editor_ps.putString("permission", " ")
                                    editor_ps.commit()
                                    val intent = Intent(this@AdminActivity, InsideActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                    boolean = true
                                }
                            })
                            var dialog: Dialog = builder.create()
                            dialog.show()
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
        var boolean: Boolean? = false
        if (item!!.itemId == R.id.DangXuat) {
            Log.e("Dang xuat ne", "abc")
            var builder: AlertDialog.Builder = AlertDialog.Builder(this)
            var inflater: LayoutInflater = layoutInflater
            var view: View = inflater.inflate(R.layout.layout_dialog, null)
            var content: TextView = view.findViewById<View>(R.id.txtDialog_content) as TextView
            content.setText("Bạn có muốn đăng xuất")
            builder.setView(view)
            builder.setNegativeButton(R.string.dialogAsk_no, object : DialogInterface.OnClickListener { // cancel
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0!!.dismiss()

                }
            })
            builder.setPositiveButton(R.string.dialogAsk_yes, object : DialogInterface.OnClickListener { // apply
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    var token = getSharedPreferences("username", Context.MODE_PRIVATE)
                    var editor = token.edit()
                    editor.putString("loginusername", " ")
                    editor.commit()
                    var token_ps = getSharedPreferences("permission", Context.MODE_PRIVATE)
                    var editor_ps = token_ps.edit()
                    editor_ps.putString("permission", " ")
                    editor_ps.commit()
                    intent = Intent(this@AdminActivity, InsideActivity::class.java)
                    startActivity(intent)
                    finish()
                    boolean = true
                }
            })
            var dialog: Dialog = builder.create()
            dialog.show()
        } else if (item!!.itemId == R.id.itemTrangCaNhan) {
            val intent_profile = Intent(this, ProfileActivity::class.java)
            startActivity(intent_profile)
            boolean = true
        } else
            boolean = super.onOptionsItemSelected(item)
        return boolean!!

    }

    /**
     * Lấy id của máy mỗi lần vào Admin
     */
    private fun displayFirebaseRegId() {
        if (mAuth != null) {
            val pref = applicationContext.getSharedPreferences("ah_firebase", 0)
            val regId = pref.getString("regId", null)
            dbFireStore.collection(Parameter.root_User)
            var washingtonRef: DocumentReference =
                    dbFireStore.collection(Parameter.root_User).document(mAuth!!.uid)
            washingtonRef.update(Parameter.comp_uidDevice, regId).addOnSuccessListener { void ->
                Log.e("tmt", "Firebase reg id: " + regId!!)
            }.addOnFailureListener { exception ->
                Log.e("tmt", "that bai")
            }
        }
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
        NotificationUtils(applicationContext).clearNotifications()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver)
        super.onPause()
    }
}
