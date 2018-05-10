package com.iuh.tranthang.myshool

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.model.Parameter
import kotlinx.android.synthetic.main.activity_notification_pan.*

class NotificationPanActivity : AppCompatActivity() {
    lateinit var db: dbConnect
    private lateinit var dbFireStore: FirebaseFirestore
    private lateinit var mAuth: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val getId = intent.getStringExtra(Parameter.KEY_ID_MESS)
        val getContent = intent.getStringExtra(Parameter.KEY_CONTENT_MESS)
        val getTitle = intent.getStringExtra(Parameter.KEY_Title_MESS)
        db = dbConnect(this)
        dbFireStore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance().currentUser!!

        Log.e("tmt activity ", getId + "  -  " + getContent)
//        db.getNotificationRealTime(getId)
        db.getNotification(getId)
        setContentView(R.layout.activity_notification_pan)

        // Tắt menu
        val actionBar = supportActionBar
        actionBar!!.hide()
        txtpop_title.text = getTitle
        txtContent.text = getContent
        btn_back.setOnClickListener { view ->
            var token_ps = getSharedPreferences("permission", Context.MODE_PRIVATE)
            dbFireStore!!.collection(Parameter.root_User).document(mAuth!!.uid!!)
                    .get().addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            var result: DocumentSnapshot = task.result
                            if (result.exists()) {
                                changeActivy(result.data[Parameter.comp_Permission].toString())
                            }
                        }
                    })
        }
    }

    private fun changeActivy(permission: String) {
        when (permission) {
            "0" -> {
                intent = Intent(this, AcountantActivity::class.java)
                startActivity(intent)
            }
            "1" -> {
                intent = Intent(this, ATeacherActivity::class.java)
                startActivity(intent)
            }
            "2" -> {
                intent = Intent(this, AStaffActivity::class.java)
                startActivity(intent)
            }
            "3" -> {
                intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            }
            else -> {
                Log.e("tmt-123123", permission + " - Lỗi")
            } // THONG BAO LOI !!!
        }
        finish()
    }
}
