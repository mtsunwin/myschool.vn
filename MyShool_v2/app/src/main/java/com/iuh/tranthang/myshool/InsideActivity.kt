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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.model.Parameter
import kotlinx.android.synthetic.main.activity_inside.*
import org.w3c.dom.Text


class InsideActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    //global variables
    private var email: String? = null
    private var password: String? = null
    private var permission: String? = ""
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
    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null

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
<<<<<<< HEAD
            Log.e("tmt login", email + " - " + password)
            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) { task ->
                        mProgressBar!!.hide()
                        if (task.isSuccessful) {
                            updateUI()
                        } else {
                            Log.e("tm", "signInWithEmail:failure", task.exception)
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
=======
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
>>>>>>> a4bd33a80e742386db7e6d8a167a8447f653e307
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
// =======
//         if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
//             Log.e("tmt login", email + " - " + password)
//             mAuth!!.signInWithEmailAndPassword(email!!, password!!)
//                     .addOnCompleteListener(this) { task ->
//                         mProgressBar!!.hide()
//                         if (task.isSuccessful) {
//                             var token = getSharedPreferences("username", Context.MODE_PRIVATE)
//                             var editor = token.edit()
//                             editor.putString("loginusername", email)
//                             editor.commit()
//                             updateUI()
//                             finish()
//                         } else {
//                             Log.e(TAG, "signInWithEmail:failure", task.exception)
//                             Toast.makeText(this, "Authentication failed.",
//                                     Toast.LENGTH_SHORT).show()
// >>>>>>> master
                        }
            }

        } else if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }*/

    private fun ForgetPassword() {
        val intent_fp: Intent = Intent(this, ForgetPasswordActivity::class.java)
        intent_fp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent_fp)
    }

    private fun updateUI() {
        val mUser = mAuth!!.currentUser
        Log.e("tmt check", "start")
        val db = FirebaseFirestore.getInstance()
        db.collection(Parameter().root_User)
                .whereEqualTo(Parameter().comp_UId, mAuth!!.uid)
                .get()
                .addOnCompleteListener({ task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            // Authentication
                            var token = getSharedPreferences("username", Context.MODE_PRIVATE)
                            var editor = token.edit()
                            editor.putString("loginusername", email)
                            editor.commit()
                            // Check Permission
                            changeActivy(document.data[Parameter().comp_Permission] as String)
                        }
                    } else {
                        Log.d("tmt", "Error getting documents: ", task.exception)
                    }
                })
    }

    private fun changeActivy(permission: String) {
        Log.e("tmt-123123", permission)
        when (permission) {
            "0" -> intent = Intent(this, ATeacherActivity::class.java)
            "1" -> intent = Intent(this, AStaffActivity::class.java)
            "2" -> intent = Intent(this, AcountantActivity::class.java)
            "3" -> intent = Intent(this, AdminActivity::class.java)
            else -> intent = Intent(this, AdminActivity::class.java) // THONG BAO LOI !!!

        }
        intent.putExtra("username", edit_username.toString())
        intent.putExtra("passwor", edit_password.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}
