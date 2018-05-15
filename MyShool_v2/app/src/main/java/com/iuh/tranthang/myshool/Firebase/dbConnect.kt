package com.iuh.tranthang.myshool.Firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.model.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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

    fun getUser() {
        dbFireStore.collection(Parameter.root_User).document(mAuth!!.uid!!)
                .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                    var mU: mUser = mUser()
                    mU.setAction(documentSnapshot.data[Parameter.comp_action] as Boolean)
                    mU.setAddress(documentSnapshot.data[Parameter.comp_address] as String)
                    mU.setBirthday(documentSnapshot.data[Parameter.comp_birthday] as String)
                    mU.setChucVu(documentSnapshot.data[Parameter.comp_chucVu] as String)
                    mU.setCoefficient(documentSnapshot.data[Parameter.comp_baseSalary] as String)
                    mU.setBirthday(documentSnapshot.data[Parameter.comp_birthday] as String)
                    mU.setEmail(documentSnapshot.data[Parameter.comp_email] as String)
                    mU.setNumberphone(documentSnapshot.data[Parameter.comp_numberphone] as String)
                    mU.setPermission(documentSnapshot.data[Parameter.comp_Permission] as String)
                    mU.setFullname(documentSnapshot.data[Parameter.comp_fullname] as String)
                    mU.setToCongTac(documentSnapshot.data[Parameter.comp_toCongTac] as String)
                    mU.setUid(documentSnapshot.data[Parameter.comp_UId] as String)
                    mU.setUrl(documentSnapshot.data[Parameter.comp_url] as String)
                    var sendNotification = act as inforUserLogin
                    sendNotification.getInfoUser(mU)
                }
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

    /**
     * Thực hiện lấy danh sách Notification
     * Trả về qua Interface getListNotification_Template
     *
     */
    fun getListNotificationTemplate() {
        dbFireStore.collection(Parameter_Notification.collection_template)
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

    fun deleteItemNotificationTemplate(id: String) {
        var sendReponse: sendReponse = act as sendReponse
        dbFireStore.collection(Parameter_Notification.collection_template)
                .document(id).delete()
                .addOnSuccessListener { void ->
                    sendReponse.deleteItemNotification_Template(true)
                }
                .addOnFailureListener { exception ->
                    sendReponse.deleteItemNotification_Template(false)
                    Log.e("tmt", "xóa không thành công" + exception.message)
                }
    }

    /**
     * Thực hiện lấy danh sách Notification đã gửi
     * Trả về qua Interface getListNotification_sent
     *
     */
    fun getListNotification() {
        dbFireStore.collection(Parameter_Notification.collection_list)
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
                    var sendReponse: sendListNotification = act as sendListNotification
                    sendReponse.getList(list)
                }
                .addOnFailureListener { exception ->
                    Log.e("tmt2", exception.toString())
                }
    }

    /**
     * Chức năng thêm thông báo khi đã gửi
     * Hàm trả về interface pushNotification
     */
    fun addNotification(id: String, title: String, content: String, group: String) {
        var con = dbFireStore.collection(Parameter_Notification.collection_list).document(id)
        var mNotifi: mNotification = mNotification(id, title,
                content, group)
        var cal = Calendar.getInstance()
        var date = cal.time
        mNotifi.count = "0"
        mNotifi.dateTime = SimpleDateFormat("dd/MM/yyyy hh:mm:ss aaa").format(date)
        mNotifi.listView = ArrayList<mNotificationUser>()
        var sendNotification = act as sendNotification
        con.set(mNotifi)
                .addOnSuccessListener { documentReference ->
                    sendNotification.pushNotification(true)
                }
        sendNotification.pushNotification(false)
    }

    /**
     * Lấy nội dung thông tin theo thời gian thực
     */
    fun getNotificationRealTime(id: String) {
        var docRef = dbFireStore.collection(Parameter_Notification.collection_list)
                .document(id.trim())
        docRef.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                return@addSnapshotListener
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                Log.e("tmt data collection", documentSnapshot.data.toString())
            }
        }
    }

    /**
     * Lấy nội dung thông tin
     */
    fun getNotification(id: String) {
        var docRef = dbFireStore.collection(Parameter_Notification.collection_list)
                .document(id.trim())
        var str = docRef.get()
        str.addOnSuccessListener { documentSnapshot ->
            Log.e("tmt data to get", documentSnapshot.data.toString())
        }
    }

    fun getListTakeLeaves() {
        dbFireStore.collection(Parameter_Take_Leaves.collection)
                .whereEqualTo(Parameter_Take_Leaves.UserId, mAuth!!.uid)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    var mList: ArrayList<mTakeLeave> = ArrayList<mTakeLeave>()
                    var returnList = act as returnList
                    for (document in querySnapshot) {
                        Log.e("qqqq", "taaodkaspodkpokds")
                        val myObject = document.toObject(mTakeLeave::class.java)
                        mList.add(myObject)
                    }
                    returnList.listTakeLeaves(mList)
                }
    }

    fun getAllListUser(type: String) {
        dbFireStore.collection(Parameter.root_User)
                .get()
                .addOnCompleteListener({ task ->
                    if (task.isSuccessful) {
                        var sendListUser = act as sendListUser
                        val listUser = ArrayList<mUser>()
                        for (document in task.result) {
                            var mUser = mUser(document.data[Parameter.comp_UId] as String,
                                    document.data[Parameter.comp_fullname] as String,
                                    document.data[Parameter.comp_Permission] as String,
                                    document.data[Parameter.comp_numberphone] as String,
                                    document.data[Parameter.comp_address] as String,
                                    document.data[Parameter.comp_email] as String,
                                    document.data[Parameter.comp_birthday] as String,
                                    document.data[Parameter.comp_toCongTac] as String,
                                    document.data[Parameter.comp_chucVu] as String,
                                    document.data[Parameter.comp_url] as String,
                                    document.data[Parameter.comp_action] as Boolean,
                                    document.data[Parameter.comp_baseSalary] as String,
                                    document.data[Parameter.comp_uidDevice] as String
                            )
                            if (type != "4") {
                                if (document.data[Parameter.comp_action].toString() == "true"
                                        && document.data[Parameter.comp_Permission] == type)
                                    listUser.add(mUser)
                            } else {
                                if (document.data[Parameter.comp_action].toString() == "true")
                                    listUser.add(mUser)
                            }
                        }
                        sendListUser.listUserDB(listUser)
                    }
                })
    }

    public fun getDonXin(key: String) {
        Log.e("qqqq", "taaodkaspodkpokds" + key)
        dbFireStore.collection(Parameter_Take_Leaves.collection)
                .whereEqualTo(Parameter_Take_Leaves.key, key)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    var mList: ArrayList<mTakeLeave> = ArrayList<mTakeLeave>()
                    var returnList = act as returnList
                    for (document in querySnapshot) {
                        Log.e("qqqq", "taaodkaspodkpokds")
                        val myObject = document.toObject(mTakeLeave::class.java)
                        mList.add(myObject)
                    }
                    returnList.listTakeLeaves(mList)
                }
    }

    interface donxin{
        fun getdonxin()
    }
    interface sendListUser {
        fun listUserDB(listUser: ArrayList<mUser>)
    }

    interface sendReponse {
        fun getListNotification_Template(list: ArrayList<mNotification>)
        fun deleteItemNotification_Template(status: Boolean)
    }

    interface sendNotification {
        fun pushNotification(status: Boolean)
    }

    interface sendListNotification {
        fun getList(list: ArrayList<mNotification>)
    }

    interface returnList {
        fun listTakeLeaves(mList: ArrayList<mTakeLeave>)
    }

    interface inforUserLogin {
        fun getInfoUser(mU: mUser)
    }
}