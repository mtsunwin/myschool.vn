package com.iuh.tranthang.myshool

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import android.widget.TextView
import com.iuh.tranthang.myshool.R.id.expandable_list_view
import com.iuh.tranthang.myshool.ViewApdater.ExpandableListAdapter
import com.iuh.tranthang.myshool.model.adm_display
import kotlinx.android.synthetic.main.activity_admin.*


class ATeacherActivity: AppCompatActivity() {

    private var drawerLayout: DrawerLayout? = null
    private var abdt: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        var token = getSharedPreferences("username", Context.MODE_PRIVATE)
        //var token_pw= getSharedPreferences("password",Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val intent = Intent(this, InsideActivity::class.java)
        val intent_profile=Intent(this,ProfileActivity::class.java)
        //edit_password.setText(token_pw.getString("loginpassword"," "))
        /* customer list view
        var arrayInforMenu: ArrayList<adm_display> = ArrayList()
        arrayInforMenu.add(adm_display("Thông tin nhân viên", R.drawable.team_group))
        listview.adapter = CustomAdapter(this, arrayInforMenu)*/


        val listHeader: ArrayList<adm_display> = ArrayList()
        listHeader.add(adm_display("Thông tin nhân viên", R.drawable.team_group, 1))
        listHeader.add(adm_display("Quản lý thông báo", R.drawable.team_group, 2))

        val inforStaff: ArrayList<adm_display> = ArrayList()
        inforStaff.add(adm_display("Danh sách tài khoản", R.drawable.team_group, 11))
        inforStaff.add(adm_display("Xem lịch biểu công việc", R.drawable.team_group, 16))


        val inforNotify: ArrayList<adm_display> = ArrayList()
        inforNotify.add(adm_display("Danh sách thông báo", R.drawable.team_group, 21))

        val fruitsList = listOf("Thang", "Nghia")
        val listChild = HashMap<String, ArrayList<adm_display>>()

        listChild.put(listHeader[0].getName(), inforStaff)
        listChild.put(listHeader[1].getName(), inforNotify)

        val expandableListAdapter = ExpandableListAdapter(this, listHeader, listChild)

        expandable_list_view.setAdapter(expandableListAdapter)

        //listview.adapter = CustomAdapter(this, arrayInforMenu)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.menuNavigation)
        abdt = ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close)

        val drawerIndicatorEnabled = abdt!!.isDrawerIndicatorEnabled
        drawerLayout!!.addDrawerListener(abdt!!)
        abdt!!.syncState()
        navigationView!!.setNavigationItemSelectedListener(
                object : NavigationView.OnNavigationItemSelectedListener {
                    override fun onNavigationItemSelected(item: MenuItem): Boolean {
                        var boolean: Boolean?=false
                        if (item!!.itemId == R.id.DangXuat) {
                            Log.e("Dang xuat ne","abc")
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this@ATeacherActivity)
                            var inflater: LayoutInflater = layoutInflater
                            var view: View = inflater.inflate(R.layout.layout_dialog, null)
                            var content: TextView = view.findViewById<View>(R.id.txtDialog_content) as TextView
                            content.setText("Bạn có muốn đăng xuất")
                            builder.setView(view)
                            builder.setNegativeButton(R.string.dialog_no, object : DialogInterface.OnClickListener { // cancel
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    p0!!.dismiss()

                                }
                            })
                            builder.setPositiveButton(R.string.dialog_yes, object : DialogInterface.OnClickListener { // apply
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
        var boolean: Boolean?=false
        if (item!!.itemId == R.id.DangXuat) {
            Log.e("Dang xuat ne","abc")
            var builder: AlertDialog.Builder = AlertDialog.Builder(this)
            var inflater: LayoutInflater = layoutInflater
            var view: View = inflater.inflate(R.layout.layout_dialog, null)
            var content: TextView = view.findViewById<View>(R.id.txtDialog_content) as TextView
            content.setText("Bạn có muốn đăng xuất")
            builder.setView(view)
            builder.setNegativeButton(R.string.dialog_no, object : DialogInterface.OnClickListener { // cancel
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0!!.dismiss()

                }
            })
            builder.setPositiveButton(R.string.dialog_yes, object : DialogInterface.OnClickListener { // apply
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
        }
        else if (item!!.itemId == R.id.itemTrangCaNhan) {
            val intent_profile=Intent(this, ProfileActivity::class.java)
            startActivity(intent_profile)
            boolean = true
        }
        else
            boolean = super.onOptionsItemSelected(item)
        return boolean!!

    }
}
