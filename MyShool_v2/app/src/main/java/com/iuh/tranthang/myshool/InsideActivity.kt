package com.iuh.tranthang.myshool

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
            //            Log.e("tmt", username.toString() + " - " + password.toString())
        })

        initialise()

    }

    private fun initialise() {
        btnLogin = findViewById<View>(R.id.btn_login) as Button
        mProgressBar = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
        btnLogin!!.setOnClickListener { loginUser() }
    }

    private fun loginUser() {

        email = edit_username.text.toString()
        password = edit_password.text.toString()

        Log.e("tmt", email + " - " + password)

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()
            Log.d(TAG, "Logging in user.")
            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) { task ->
                        mProgressBar!!.hide()
                        if (task.isSuccessful) {
                            // Sign in success, update UI with signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            var token = getSharedPreferences("username", Context.MODE_PRIVATE)
                            var editor = token.edit()
                            //var editor_pw= token_pw.edit()
                            editor.putString("loginusername", email)
                            //editor_pw.putString("loginpassword",password)
                            editor.commit()
                            // editor_pw.commit()
                            finish()
                            updateUI()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateUI() {
        val intent = Intent(this, AdminActivity::class.java)
        intent.putExtra("username", edit_username.toString())
        intent.putExtra("passwor", edit_password.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
