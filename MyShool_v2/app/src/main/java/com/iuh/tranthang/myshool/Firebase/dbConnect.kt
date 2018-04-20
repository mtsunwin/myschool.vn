package com.iuh.tranthang.myshool.Firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User

/**
 * Created by ThinkPad on 4/20/2018.
 */
class dbConnect {
    private var mAuth: FirebaseAuth? = null
    private var dbFireStore: FirebaseFirestore? = null
    private var user: User? = null

    constructor() {
        mAuth = FirebaseAuth.getInstance()
        dbFireStore = FirebaseFirestore.getInstance()
        user = User()
    }

    private fun setUser(mUser: User) {
        this.user!!.setAddress(mUser.getAddress())
    }

    /**
     *  Check Xem con login hay không
     */
    public fun isAuthentication(): Boolean {
        var cmAuth = mAuth!!.currentUser
        if (cmAuth != null)
            return true
        return false
    }

    public fun query() {

    }

    public fun getCollection(collectionName: String) {
        var docRef: CollectionReference = dbFireStore!!.collection(collectionName)

    }

    fun getDocument(collectionName: String, documentName: String) {
        Log.e("tmt db 123", "oke đã thấy")
        var docRef = dbFireStore!!.collection(collectionName).document(documentName)
                .get().addOnCompleteListener({ task ->
                    if (task.isSuccessful) {
                        var result: DocumentSnapshot = task.result
                        if (result.exists()) {
                            Log.e("tmt in db", result.data[Parameter().comp_address].toString())
                            var tUser = User()
                            tUser!!.setAddress(result.data[Parameter().comp_address].toString())
                            this.setUser(tUser)
                        }
                    }
                })
        Log.e("tmt db 123", user!!.getAddress() + " 123")
    }


}