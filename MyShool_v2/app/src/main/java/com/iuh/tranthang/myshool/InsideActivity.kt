package com.iuh.tranthang.myshool

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.model.Parameter
import kotlinx.android.synthetic.main.activity_inside.*


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

    private var txtErrorUsername: TextView? = null
    private var txtErrorPassword: TextView? = null

    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null

    private var awesomeValidation: AwesomeValidation? = null
    private var permissionForLogIn: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside)
        var token = getSharedPreferences("username", Context.MODE_PRIVATE)
        var token_ps = getSharedPreferences("permission", Context.MODE_PRIVATE)

        Log.e("permission abc:", token_ps!!.getString("permission", " "))
        permissionForLogIn = token_ps!!.getString("permission", " ")
        Log.e("permission abc:", permissionForLogIn)

        if (token!!.getString("loginusername", " ") != " ") {
            when (permissionForLogIn) {
                "0" -> {
                    intent = Intent(this, ATeacherActivity::class.java)
                    startActivity(intent)
                }
                "1" -> {
                    intent = Intent(this, AStaffActivity::class.java)
                    startActivity(intent)
                }
                "2" -> {
                    intent = Intent(this, AcountantActivity::class.java)
                    startActivity(intent)
                }
                "3" -> {
                    intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                }
            }
            startActivity(intent)
            finish()
        }
        //Ẩn Menubar
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

        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        awesomeValidation!!.addValidation(this, R.id.edit_username, Patterns.EMAIL_ADDRESS, R.string.usernameLogInError)
        awesomeValidation!!.addValidation(this, R.id.edit_password, "[A-Za-z0-9]{6,}", R.string.PasswordLogInError)
        if (awesomeValidation!!.validate()) {
            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) { task ->
                        mProgressBar!!.hide()
                        if (task.isSuccessful) {
                            updateUI()
                        } else {
                            Log.e("tm", "signInWithEmail:failure", task.exception)
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    private fun ForgetPassword() {
        val intent_fp: Intent = Intent(this, ForgetPasswordActivity::class.java)
        intent_fp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent_fp)
    }

    private fun updateUI() {
        val db = FirebaseFirestore.getInstance()
        db.collection(Parameter().root_User)
                .whereEqualTo(Parameter().comp_UId, mAuth!!.uid)
                .get()
                .addOnCompleteListener({ task ->
                    Log.e("tmt addd event", "oke")
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            // Authentication
                            var token = getSharedPreferences("username", Context.MODE_PRIVATE)
                            var editor = token.edit()
                            editor.putString("loginusername", email)
                            editor.putString("password",password)
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
        val token_ps = getSharedPreferences("permission", Context.MODE_PRIVATE)
        var editor_ps = token_ps.edit()
        editor_ps.putString("permission", permission.toString())
        editor_ps.commit()
        when (permission) {
            "0" -> {
                intent = Intent(this, ATeacherActivity::class.java)
                startActivity(intent)
            }
            "1" -> {
                intent = Intent(this, AStaffActivity::class.java)
                startActivity(intent)
            }
            "2" -> {
                intent = Intent(this, AcountantActivity::class.java)
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
