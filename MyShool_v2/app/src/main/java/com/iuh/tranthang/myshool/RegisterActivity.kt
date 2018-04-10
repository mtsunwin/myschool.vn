package com.iuh.tranthang.myshool

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private var fullname: EditText? = null
    private var username: EditText? = null
    private var password: EditText? = null
    private var btnLogin: Button? = null
    private var mProgressBar: ProgressDialog? = null

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private var txtFullname: String? = ""
    private var txtUsername: String? = ""
    private var txtPassword: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initialise()

    }


    private fun initialise() {
        fullname = findViewById<View>(R.id.fullname) as EditText?
        username = findViewById<View>(R.id.username) as EditText?
        password = findViewById<View>(R.id.password) as EditText?
        btnLogin = findViewById<View>(R.id.btnRegister) as Button?

        mProgressBar = ProgressDialog(this)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        btnLogin!!.setOnClickListener { view -> createNewAccount() }
    }

    private fun createNewAccount() {
        txtFullname = fullname?.text.toString()
        txtUsername = username?.text.toString()
        txtPassword = password?.text.toString()

        mProgressBar!!.setMessage("Registering User...")
        mProgressBar!!.show()
        Log.e("tmt", txtUsername.toString() + txtPassword.toString())
        if(TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtPassword)){
            mProgressBar!!.hide()
            Toast.makeText(this, "Username and password not empty",Toast.LENGTH_SHORT).show()
        }else{
            if(txtPassword!!.length<6){
                mProgressBar!!.hide()
                Toast.makeText(this, "Password characters >6",Toast.LENGTH_SHORT).show()

            }else{
                mAuth!!.createUserWithEmailAndPassword(txtUsername!!, txtUsername!!)
                        .addOnCompleteListener(this) { task ->
                            mProgressBar!!.hide()
                            if (task.isSuccessful) {

                                // Sign in success, update UI with the signed-in user's information
                                Log.d("tmt", "createUserWithEmail:success")
                                val userId = mAuth!!.currentUser!!.uid

                                // Verify Email
                                // verifyEmail();

                                // update user profile information
                                val currentUserDb = mDatabaseReference!!.child(userId)
                                currentUserDb.child("fullname").setValue(txtFullname)
                                updateUserInfoAndUI()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("tmt", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(this@RegisterActivity, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()
                            }
                        }

            }

        }


    }

    private fun updateUserInfoAndUI() {
        var intent: Intent = Intent(this@RegisterActivity, AdminActivity::class.java)
        Toast.makeText(this@RegisterActivity, R.string.created_success, Toast.LENGTH_LONG)
        startActivity(intent)
    }

//    private fun verifyEmail() {
//        val mUser = mAuth!!.currentUser;
//        mUser!!.sendEmailVerification()
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(this@RegisterActivity,
//                                "Verification email sent to " + mUser.getEmail(),
//                                Toast.LENGTH_SHORT).show()
//                    } else {
//                        Log.e("tmt", "sendEmailVerification", task.exception)
//                        Toast.makeText(this@RegisterActivity,
//                                "Failed to send verification email.",
//                                Toast.LENGTH_SHORT).show()
//                    }
//                }
//    }
}

