package com.iuh.tranthang.myshool

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.model.Parameter_Take_Leaves
import com.iuh.tranthang.myshool.model.mTakeLeave

class ListTakeLeavesActivity : AppCompatActivity() {

    private lateinit var dbFireStore: FirebaseFirestore
    private lateinit var mAuth: FirebaseUser
    private lateinit var mList: ArrayList<mTakeLeave>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_take_leaves)

        val actionBar = supportActionBar
        actionBar!!.hide()
        mList = ArrayList<mTakeLeave>()

        dbFireStore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance().currentUser!!

        dbFireStore.collection(Parameter_Take_Leaves.collection).whereEqualTo(Parameter_Take_Leaves.UserId, mAuth.uid)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    for (document in querySnapshot) {
                        Log.e("qqqq", "taaodkaspodkpokds")
                        val myObject = document.toObject(mTakeLeave::class.java)
                        mList.add(myObject)
                    }
                }
    }
}
