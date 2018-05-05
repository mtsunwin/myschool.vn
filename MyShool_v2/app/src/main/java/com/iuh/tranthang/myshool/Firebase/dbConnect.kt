package com.iuh.tranthang.myshool.Firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.model.*


/**
 * Created by ThinkPad on 4/20/2018.
 */
class dbConnect {
    private var mAuth: FirebaseAuth? = null
    private lateinit var dbFireStore: FirebaseFirestore
    private var mUser: mUser? = null
    private lateinit var act: Activity


    constructor(ac: Activity) {
        mAuth = FirebaseAuth.getInstance()
        dbFireStore = FirebaseFirestore.getInstance()
        mUser = mUser()
        act = ac
    }

    constructor() {
        mAuth = FirebaseAuth.getInstance()
        dbFireStore = FirebaseFirestore.getInstance()
        mUser = mUser()
    }

    private fun setUser(mMUser: mUser) {
        this.mUser!!.setAddress(mMUser.getAddress())
    }

    fun getUser(): String {
        return this.mUser!!.getAddress()
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

    public fun getUid(): String {
        if (isAuthentication())
            return mAuth!!.uid!!
        else
            return ""
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
                            Log.e("tmt in db", result.data[Parameter.comp_address].toString())
                            var tUser = mUser()
                            tUser!!.setAddress(result.data[Parameter.comp_address].toString())
                            this.setUser(tUser)
                        }
                    }
                })
    }

    fun getListNotificationTemplate() {
        dbFireStore.collection(Parameter_Notification.collection)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    var list: ArrayList<mNotification> = ArrayList<mNotification>()
                    for (obj in querySnapshot.documents) {
                        var mNoti: mNotification = mNotification(obj.getString(Parameter_Notification.field_id),
                                obj.getString(Parameter_Notification.field_title),
                                obj.getString(Parameter_Notification.field_content),
                                obj.getString(Parameter_Notification.field_group))
                        mNoti.dateTime = obj.getString(Parameter_Notification.field_dateTime)
                        mNoti.count = obj.getString(Parameter_Notification.field_count)
                        var inU: ArrayList<mNotificationUser>
                        inU = obj.get(Parameter_Notification.field_listView) as ArrayList<mNotificationUser>
                        var temp = inU.size
                        if (temp > 0) {
                            var mUB: ArrayList<mNotificationUser> = ArrayList<mNotificationUser>()
                            while (temp > 0) {
                                var m: HashMap<String, String> = inU.get(temp - 1) as HashMap<String, String>
                                var mU: mNotificationUser = mNotificationUser(
                                        m.get(Parameter_Notification.field_listView_id).toString(),
                                        m.get(Parameter_Notification.field_listView_datetime).toString(),
                                        m.get(Parameter_Notification.field_listView_name).toString()
                                )
                                mUB.add(mU)
                                temp--;
                            }
                            mNoti.listView = mUB
                        }
                        list.add(mNoti)
                    }
                    Log.e("tmt lits", list.size.toString() + " list")
                    var sendReponse: sendReponse = act as sendReponse
                    sendReponse.getListNotification_Template(list)
                }
                .addOnFailureListener { exception ->
                    Log.e("tmt2", exception.toString())
                }
    }

    public interface sendReponse {
        fun getListNotification_Template(list: ArrayList<mNotification>)
    }
}