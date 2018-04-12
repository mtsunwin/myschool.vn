package com.iuh.tranthang.myshool

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User

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
            var dbUser = mDatabase!!.child(Parameter().getDbNodeUser())
            var dbInfo = dbUser.child(mAuth!!.uid).child(Parameter().getdbNodeInfor())

            dbInfo.addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                    var message = dataSnapshot!!.getValue(User::class.java)
                    Log.e("tmt", message.toString())
                    messageList.add(message!!)
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
        }
    }
}
