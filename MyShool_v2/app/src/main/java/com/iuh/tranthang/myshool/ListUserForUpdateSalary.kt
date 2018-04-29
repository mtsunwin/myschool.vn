package com.iuh.tranthang.myshool

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.ViewApdater.DataAdapter
import com.iuh.tranthang.myshool.ViewApdater.SimpleAdapter
import com.iuh.tranthang.myshool.ViewApdater.SwipeToDeleteCallback
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User
import kotlinx.android.synthetic.main.activity_list_user.*

class ListUserForUpdateSalary : AppCompatActivity() {
    private var mAuth: FirebaseUser? = null
    private var recyclerView: RecyclerView? = null

    val listUser = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        mAuth = FirebaseAuth.getInstance().currentUser
        firebaseListenerInit()
    }
    private fun firebaseListenerInit() {
        if (mAuth != null) {
            Log.e("tmt data", "it will run this")
            val db = FirebaseFirestore.getInstance()
            db.collection(Parameter().root_User)
                    .get()
                    .addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            listUser.clear()
                            for (document in task.result) {
                                var mUser = User(document.data[Parameter().comp_UId] as String,
                                        document.data[Parameter().comp_fullname] as String,
                                        document.data[Parameter().comp_Permission] as String,
                                        document.data[Parameter().comp_numberphone] as String,
                                        document.data[Parameter().comp_address] as String,
                                        document.data[Parameter().comp_email] as String,
                                        document.data[Parameter().comp_birthday] as String,
                                        document.data[Parameter().comp_toCongTac] as String,
                                        document.data[Parameter().comp_chucVu] as String,
                                        document.data[Parameter().comp_url] as String,
                                        document.data[Parameter().comp_action] as Boolean,
                                        document.data[Parameter().comp_salary] as String
                                )
                                if (document.data[Parameter().comp_action].toString() == "true")
                                    listUser.add(mUser)
                            }
                            callAdapter(listUser)
                        } else {
                            Log.d("tmt", "Error getting documents: ", task.exception)
                            // Lỗi trả về
                        }
                    })
        }

    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        var inflater :MenuInflater=menuInflater
        inflater.inflate(R.menu.context_menu_updatesalary,menu)
        menu!!.add(0,v!!.id,0,"Cập nhật hệ số lương")
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){
            R.id.updateHeSoluong->{

            }
        }
        return super.onContextItemSelected(item)

    }
    private fun callAdapter(listUser: ArrayList<User>) {
        recyclerView = findViewById<RecyclerView>(R.id.recycle) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        var adapter = DataAdapter(listUser)
        val simpleAdapter = SimpleAdapter(listUser)
        recyclerView!!.adapter = simpleAdapter
        adapter!!.notifyDataSetChanged()

    }
}

