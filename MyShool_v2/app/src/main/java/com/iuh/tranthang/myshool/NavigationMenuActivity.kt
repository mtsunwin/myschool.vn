package com.iuh.tranthang.myshool

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text


class NavigationMenuActivity : AppCompatActivity() {
    private var navigationView: NavigationView? = null
    private var view: View? = null
    private var txtDangXuat: Text? = null
    private var nav_header_imgAvartar: ImageView?=null
    private var nav_header_txtName:TextView?=null
    private var nav_header_txtPermission:TextView?=null
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        var token = getSharedPreferences("username", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.menu.navigation_menu)
        navigationView = findViewById<NavigationView>(R.id.menuNavigation) as NavigationView
        view = navigationView!!.getHeaderView(0)
        nav_header_imgAvartar= view!!.findViewById(R.id.nav_header_imageView)
        nav_header_txtName =view!!.findViewById(R.id.nav_header_name)
        nav_header_txtPermission=view!!.findViewById(R.id.nav_header_permission)
        navigationView!!.setOnClickListener { view ->
            when (view.id) {
                R.id.DangXuat -> {
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
                            intent = Intent(applicationContext, InsideActivity::class.java)
                            startActivity(intent)
                            finish()

                        }
                    })
                    var dialog: Dialog = builder.create()
                    dialog.show()
                }
                R.id.itemTrangCaNhan -> {
                    var intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }
}
