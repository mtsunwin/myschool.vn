package com.iuh.tranthang.myshool

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.ViewApdater.CustomAdapter
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User
import kotlinx.android.synthetic.main.activity_list_user.*
import java.util.*


class ListUserActivity : AppCompatActivity() {


    private var mAuth: FirebaseUser? = null
    private var mDatabase: DatabaseReference? = null
    private var mMessageReference: DatabaseReference? = null

    val listUser = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        mAuth = FirebaseAuth.getInstance().currentUser

        firebaseListenerInit()

    }

    private fun firebaseListenerInit() {
        if (mAuth != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection(Parameter().root_User)
                    .get()
                    .addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            Log.e("tmt data", task.result.size().toString())
                            for (document in task.result) {
                                listUser.add(User(document.data[Parameter().comp_UId] as String,
                                        document.data[Parameter().comp_fullname] as String,
                                        document.data[Parameter().comp_Permission] as String,
                                        document.data[Parameter().comp_numberphone] as String,
                                        document.data[Parameter().comp_address] as String,
                                        document.data[Parameter().comp_email] as String,
                                        document.data[Parameter().comp_birthday] as String))
                                Log.e("tmt item", document.data[Parameter().comp_fullname] as String)
                            }
                            callAdapter(listUser)
                        } else {
                            Log.d("tmt", "Error getting documents: ", task.exception)
                        }
                    })

        }
    }

    private fun callAdapter(listUser: ArrayList<User>) {
        val setAdap = CustomAdapter(applicationContext, listUser)
        list_user_recycleview.adapter = setAdap
    }
}