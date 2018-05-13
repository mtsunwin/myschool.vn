package com.iuh.tranthang.myshool

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.NavigationView
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.ViewApdater.ExpandableListAdapter
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.adm_display
import kotlinx.android.synthetic.main.activity_admin.*
import java.io.File


class ATeacherActivity : AppCompatActivity() {

    private var drawerLayout: DrawerLayout? = null
    private var abdt: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null

    //bien cho hien thi avatar navigation
    private lateinit var dbFireStore: FirebaseFirestore
    private lateinit var mAuth: FirebaseUser
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
        var token = getSharedPreferences("username", Context.MODE_PRIVATE)
        //var token_pw= getSharedPreferences("password",Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        Log.e("activity", "Đã vào activity teacher")

        // Khởi tạo các đối tượng giao tiếp với firebase
        mAuth = FirebaseAuth.getInstance().currentUser!!
        dbFireStore = FirebaseFirestore.getInstance()
        val intent = Intent(this, InsideActivity::class.java)
        val intent_profile = Intent(this, ProfileActivity::class.java)
        //edit_password.setText(token_pw.getString("loginpassword"," "))
        /* customer list view
        var arrayInforMenu: ArrayList<adm_display> = ArrayList()
        arrayInforMenu.add(adm_display("Thông tin nhân viên", R.drawable.team_group))
        listview.adapter = AdapterDataUser(this, arrayInforMenu)*/


        val listHeader: ArrayList<adm_display> = ArrayList()
        listHeader.add(adm_display("Thông tin nhân viên", R.drawable.ic_communication, 1))
        listHeader.add(adm_display("Thông báo", R.drawable.ic_notification, 2))
        listHeader.add(adm_display("Đơn xin phép", R.drawable.ic_communication, 3))

        val inforStaff: ArrayList<adm_display> = ArrayList()
        inforStaff.add(adm_display("Tất cả", R.drawable.ic_clipboard, 11))
        inforStaff.add(adm_display("Giáo viên", R.drawable.ic_clipboard, 12))
        inforStaff.add(adm_display("Quản lý", R.drawable.ic_clipboard, 13))
        inforStaff.add(adm_display("Kế toán", R.drawable.ic_clipboard, 14))
        inforStaff.add(adm_display("Xem lịch biểu công việc", R.drawable.ic_calendar_1, 16))

        val inforNotify: ArrayList<adm_display> = ArrayList()
        inforNotify.add(adm_display("Danh sách thông báo", R.drawable.ic_list_2, 21))

        val inforLichBieu: ArrayList<adm_display> = ArrayList()
        inforLichBieu.add(adm_display("Thông tin", R.drawable.ic_list_2, 32))
        inforLichBieu.add(adm_display("Xin nghỉ phép", R.drawable.ic_list_2, 31))


        val listChild = HashMap<String, ArrayList<adm_display>>()

        listChild.put(listHeader[0].getName(), inforStaff)
        listChild.put(listHeader[1].getName(), inforNotify)
        listChild.put(listHeader[2].getName(), inforLichBieu)

        val expandableListAdapter = ExpandableListAdapter(this, listHeader, listChild)

        expandable_list_view.setAdapter(expandableListAdapter)

        //listview.adapter = AdapterDataUser(this, arrayInforMenu)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.menuNavigation)
        abdt = ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close)


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
                                Log.e("Name+permission", name + "----" + permission)
                                nav_header_txtName!!.setText(name.toString())
                                nav_header_txtPermission!!.setText(permission.toString())
                                txtURLImage = result.data[Parameter.comp_url].toString()
                                Log.e("URL:", txtURLImage.toString())
                                if (txtURLImage!!.length > 0) {
                                    try {
                                        val tmpFile = File.createTempFile("img", "png")
                                        val reference = FirebaseStorage.getInstance().getReference("images/")

                                        //  "id" is name of the image file....

                                        reference.child(txtURLImage.toString()).getFile(tmpFile).addOnSuccessListener({
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


        val drawerIndicatorEnabled = abdt!!.isDrawerIndicatorEnabled
        drawerLayout!!.addDrawerListener(abdt!!)
        abdt!!.syncState()
        navigationView!!.setNavigationItemSelectedListener(
                object : NavigationView.OnNavigationItemSelectedListener {
                    override fun onNavigationItemSelected(item: MenuItem): Boolean {
                        var boolean: Boolean? = false
                        if (item!!.itemId == R.id.DangXuat) {
                            Log.e("Dang xuat ne", "abc")
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this@ATeacherActivity)
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
                                    val intent = Intent(this@ATeacherActivity, InsideActivity::class.java)
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
                    intent = Intent(this@ATeacherActivity, InsideActivity::class.java)
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
}
