package com.iuh.tranthang.myshool

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.ViewApdater.DataAdapter
import com.iuh.tranthang.myshool.ViewApdater.SimpleAdapter
import com.iuh.tranthang.myshool.ViewApdater.SwipeToDeleteCallback
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User
import kotlinx.android.synthetic.main.activity_list_user.*
import java.util.*


class ListUserActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {


    private var mAuth: FirebaseUser? = null
    private var recyclerView: RecyclerView? = null

    val listUser = ArrayList<User>()

    /**
     * Swipe Refresh
     */
    override fun onRefresh() {
        Toast.makeText(this, "Showw", Toast.LENGTH_LONG).show()
        firebaseListenerInit()
        swipe_container.setRefreshing(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        mAuth = FirebaseAuth.getInstance().currentUser
        swipe_container.setOnRefreshListener { onRefresh() }
        swipe_container.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
        firebaseListenerInit()
    }

    /**
     * Lấy danh sách User từ Firebase
     */
    private fun firebaseListenerInit() {
        if (mAuth != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection(Parameter().root_User)
                    .get()
                    .addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            Log.e("tmt data", task.result.size().toString())
                            for (document in task.result) {
                                var mUser = User(document.data[Parameter().comp_UId] as String,
                                        document.data[Parameter().comp_fullname] as String,
                                        document.data[Parameter().comp_Permission] as String,
                                        document.data[Parameter().comp_numberphone] as String,
                                        document.data[Parameter().comp_address] as String,
                                        document.data[Parameter().comp_email] as String,
                                        document.data[Parameter().comp_birthday] as String,
                                        document.data[Parameter().comp_toCongTac] as String,
                                        document.data[Parameter().comp_chucVu] as String)
                                var temp: Boolean = false
                                for (cUser in listUser) {
                                    if (cUser.getUid() == mUser.getUid()) {
                                        cUser.setAddress(mUser.getAddress())
                                        cUser.setBirthday(mUser.getBirthday())
                                        cUser.setEmail(mUser.getEmail())
                                        cUser.setFullname(mUser.getFullname())
                                        temp = true
                                    }
                                }
                                if (!temp) {
                                    listUser.add(mUser)
                                    Log.e("tmt add", "oke")
                                }
                            }
                            callAdapter(listUser)
                        } else {
                            Log.d("tmt", "Error getting documents: ", task.exception)
                            // Lỗi trả về
                        }
                    })
        }
    }

    /*
    * Khơi tạo Adapter và danh sách
    * */
    private fun callAdapter(listUser: ArrayList<User>) {

//        val setAdap = CustomAdapter(applicationContext, listUser)
//        list_user_recycleview.adapter = setAdap

        recyclerView = findViewById<RecyclerView>(R.id.recycle) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        var adapter = DataAdapter(listUser)
        val simpleAdapter = SimpleAdapter(listUser)
        recyclerView!!.adapter = simpleAdapter
        adapter!!.notifyDataSetChanged()

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView!!.adapter as SimpleAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        fab.setOnClickListener { view ->
            //            simpleAdapter.addItem("New item")
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}