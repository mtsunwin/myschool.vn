package com.iuh.tranthang.myshool

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_inside.*
import org.w3c.dom.Text

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
    private var txtErrorUsername :TextView? =null
    private var txtErrorPassword :TextView? =null
    private lateinit var mDatabase: DatabaseReference

    //var token_pw= getSharedPreferences("password",Context.MODE_PRIVATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside)

        txtErrorUsername=findViewById<TextView>(R.id.txtErrorUsername)
        txtErrorPassword=findViewById(R.id.txtErrorPassword)
        edit_username.setOnClickListener{
            txtErrorUsername!!.setText("")
        }
        edit_password.setOnClickListener{
            txtErrorPassword!!.setText("")
        }
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
        if (!TextUtils.isEmpty(email))
            txtErrorUsername!!.setText("Not empty")
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtErrorUsername!!.setText("Wrong format email.")
        }
        else txtErrorUsername!!.setText("")
        if (password!!.length>0||password!!.length<6)
            txtErrorPassword!!.setText("Not empty and characters more than 6")
        if(password!!.length>=6)
            txtErrorPassword!!.setText("")
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Wrong format email.",
                        Toast.LENGTH_SHORT).show()
            } else if (password!!.length < 6) {
                Toast.makeText(this, "Passwords characters > 6",
                        Toast.LENGTH_SHORT).show()
            } else {
                mProgressBar!!.setMessage("Logging User...")
                mProgressBar!!.show()
                Log.d(TAG, "Logging in user.")
                mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                        .addOnCompleteListener(this) { task ->
                            mProgressBar!!.hide()
                            if (task.isSuccessful) {
                                // Sign in success, update UI with signed-in user's information
                                // Log.d(TAG, "signInWithEmail:success")
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
            }
        }
    }
    /*private fun loginUser() {

        email = edit_username.text.toString()
        password = edit_password.text.toString()

        Log.e("tmt", email + " - " + password)

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(this, "Wrong format email.",
                        Toast.LENGTH_SHORT).show()
            }
            else if(password!!.length<6){
                Toast.makeText(this, "Passwords characters > 6",
                        Toast.LENGTH_SHORT).show()
            }
            else{
                mProgressBar!!.setMessage("Logging User...")
                mProgressBar!!.show()
                Log.d(TAG, "Logging in user.")
                mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                        .addOnCompleteListener(this) { task ->
                            mProgressBar!!.hide()
                            if (task.isSuccessful) {
                                // Sign in success, update UI with signed-in user's information
                                // Log.d(TAG, "signInWithEmail:success")
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
            }

        } else if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }*/


    private fun updateUI() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.child("Users").toString()
                Log.e("tmt", result)
            }

        })
        val intent = Intent(this, AdminActivity::class.java)
        intent.putExtra("username", edit_username.toString())
        intent.putExtra("passwor", edit_password.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
