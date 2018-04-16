package com.iuh.tranthang.myshool

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.ViewApdater.CustomAdapter
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User
import kotlinx.android.synthetic.main.activity_list_user.*
import java.util.*


class ListUserActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private var swipeLayout: SwipeRefreshLayout? = null
    /**
     * Swipe Refresh
     */
    override fun onRefresh() {
        Toast.makeText(this, "Showw", Toast.LENGTH_LONG)
        firebaseListenerInit()
        hideLoading()
    }

    /**
     * Hide Swipe
     */
    private fun hideLoading() {
        swipeLayout!!.isRefreshing = false
    }


    private var mAuth: FirebaseUser? = null
    private var mDatabase: DatabaseReference? = null
    private var mMessageReference: DatabaseReference? = null

    val listUser = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        mAuth = FirebaseAuth.getInstance().currentUser

        firebaseListenerInit()

        swipeLayout = findViewById<View>(R.id.swipe_container) as SwipeRefreshLayout?
        swipeLayout!!.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark)
        swipeLayout!!.setOnRefreshListener { this }
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
                                        document.data[Parameter().comp_birthday] as String)
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

    private fun callAdapter(listUser: ArrayList<User>) {
        val setAdap = CustomAdapter(applicationContext, listUser)
        list_user_recycleview.adapter = setAdap
    }
}