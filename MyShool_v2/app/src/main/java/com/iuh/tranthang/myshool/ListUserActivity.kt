package com.iuh.tranthang.myshool

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User
import com.google.firebase.database.GenericTypeIndicator




class ListUserActivity : AppCompatActivity() {


    private var mAuth: FirebaseUser? = null
    private var mDatabase: DatabaseReference? = null
    private var mMessageReference: DatabaseReference? = null

    val messageList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        mDatabase = FirebaseDatabase.getInstance().reference
        mMessageReference = FirebaseDatabase.getInstance().getReference("Infor")
        mAuth = FirebaseAuth.getInstance().currentUser

        firebaseListenerInit()

        Log.e("tmt", messageList.toString())

    }

    private fun firebaseListenerInit() {
        if (mAuth != null) {
            var mUserReference = mDatabase!!.child(Parameter().getDbNodeUser())
            mUserReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
//                    val e = Log.e("tmt", snapshot!!.toString())
//                    var branch = snapshot.child(mAuth!!.uid)
//
//                    val td = snapshot.getValue() as HashMap<String, Any>
//
//                    Log.e("tmt list", td["infor"].toString())
                    val t = object : GenericTypeIndicator<List<String>>() {}
                    val hashMap = snapshot!!.getValue(t)
                    if (hashMap != null) {
                        for (entry in hashMap) {
                            val educations = entry.value
                            for (education in educations) {
                                Log.e("tmt", education.toString())
                            }
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }
    }
}
