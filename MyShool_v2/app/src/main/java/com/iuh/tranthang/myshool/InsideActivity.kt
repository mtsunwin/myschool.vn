package com.iuh.tranthang.myshool

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_inside.*

class InsideActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    //global variables
    private var email: String? = null
    private var password: String? = null
    //UI elements
    private var tvForgotPassword: TextView? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnLogin: Button? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressDialog? = null
    //Firebase references
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null


    //var token_pw= getSharedPreferences("password",Context.MODE_PRIVATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside)
        var token = getSharedPreferences("username", Context.MODE_PRIVATE)
        if (token!!.getString("loginusername", " ") != " ") {
            var intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }
        //        Ẩn Menubar
        supportActionBar!!.hide()
        btn_login.setOnClickListener(View.OnClickListener {
            Log.e("tmt", "sign in")
            loginUser()
        })
        btn_forgot_password.setOnClickListener(View.OnClickListener {
            Log.e("tmt", "click")
            ForgetPassword()
        })
        initialise()
    }

    private fun initialise() {
        // Firebase
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        // Comp
        btnLogin = findViewById<View>(R.id.btn_login) as Button
        mProgressBar = ProgressDialog(this)
        btnLogin!!.setOnClickListener { loginUser() }
    }

    private fun loginUser() {
        email = edit_username.text.toString()
        password = edit_password.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            Log.e("tmt login", email + " - " + password)
            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) { task ->
                        mProgressBar!!.hide()
                        if (task.isSuccessful) {
                            var token = getSharedPreferences("username", Context.MODE_PRIVATE)
                            var editor = token.edit()
                            editor.putString("loginusername", email)
                            editor.commit()
                            finish()
                            updateUI()
                        } else {
                            Log.e(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ForgetPassword() {
        val intent_fp: Intent = Intent(this, ForgetPasswordActivity::class.java)
        intent_fp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent_fp)
    }

    private fun updateUI() {
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)
        // Get Email User
        Log.d("tmt name:", mUser.email)
        Log.d("tmt name:", mUser.isEmailVerified.toString())
        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                Log.e("tmt - new", snapshot!!.child("fullname").value as String)
            }

        })
        val intent = Intent(this, AdminActivity::class.java)
        intent.putExtra("username", edit_username.toString())
        intent.putExtra("passwor", edit_password.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
