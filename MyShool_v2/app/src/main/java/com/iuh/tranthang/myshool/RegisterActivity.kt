package com.iuh.tranthang.myshool

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User


class RegisterActivity : AppCompatActivity() {

    private var fullname: EditText? = null
    private var username: EditText? = null
    private var password: EditText? = null
    private var address: EditText? = null
    private var numberphone: EditText? = null
    private var birthday: EditText? = null

    private var btnLogin: Button? = null
    private var mProgressBar: ProgressDialog? = null

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private var txtFullname: String? = ""
    private var txtUsername: String? = ""
    private var txtPassword: String? = ""
    private var txtAddress: String? = ""
    private var txtNumberphone: String? = ""
    private var txtBirthday: String? = ""
    private var txtPermission: String? = ""
    private var spinnerPermisstion: Spinner? = null
    private var intPermisstion: Int? = 0
    private var txtErrorUserName: TextView? = null
    private var txtErrorPassword: TextView? = null

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
        address = findViewById<View>(R.id.address) as EditText?
        numberphone = findViewById<View>(R.id.numberphone) as EditText?
        birthday = findViewById<View>(R.id.birthday) as EditText?
        txtErrorUserName = findViewById<TextView>(R.id.ErrorUserName_register)
        txtErrorPassword = findViewById<TextView>(R.id.ErrorPassword_register)
        spinnerPermisstion = findViewById<View>(R.id.selectPermission) as Spinner?

        spinnerPermisstion!!.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.select_permission))

        mProgressBar = ProgressDialog(this)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        username!!.setOnClickListener {
            txtErrorUserName!!.setText("")
        }
        password!!.setOnClickListener {
            txtErrorPassword!!.setText("")
        }
        btnLogin!!.setOnClickListener { view ->
            if (password!!.text.length < 6)
                txtErrorPassword!!.setText("Password characters must be more than 6")
            if (!Patterns.EMAIL_ADDRESS.matcher(username!!.text.toString()).matches()) {
                txtErrorUserName!!.setText("Wrong format email.")
            }
            if (txtErrorUserName!!.text.length > 0 || txtErrorPassword!!.text.length > 0)
                Toast.makeText(this, "Input complete username and password", Toast.LENGTH_SHORT).show()
            else
                createNewAccount()
        }
    }

    private fun createNewAccount() {
        txtFullname = fullname?.text.toString()
        txtUsername = username?.text.toString()
        txtPassword = password?.text.toString()
        txtAddress = address?.text.toString()
        txtBirthday = birthday?.text.toString()
        txtNumberphone = numberphone?.text.toString()
        txtPermission = spinnerPermisstion?.getSelectedItem().toString()

        val listStringPermission = resources.getStringArray(R.array.select_permission)
        Log.e("tmt list", listStringPermission[0])
        when (txtPermission!!.toLowerCase().trim()) {
            listStringPermission[0].toLowerCase().trim() -> intPermisstion = 0 //Giao vien
            listStringPermission[1].toLowerCase().trim() -> intPermisstion = 1 //Nhan vien
            listStringPermission[2].toLowerCase().trim() -> intPermisstion = 2 //Ke toan
            listStringPermission[3].toLowerCase().trim() -> intPermisstion = 3 //Admin
        }

        if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtPassword)) {
            mProgressBar!!.hide()
            Toast.makeText(this, "Username and password not empty", Toast.LENGTH_SHORT).show()
        } else {
            if (txtPassword!!.length < 6) {
                mProgressBar!!.hide()
                Toast.makeText(this, "Password characters >6", Toast.LENGTH_SHORT).show()

            } else {
                mAuth!!.createUserWithEmailAndPassword(txtUsername!!, txtPassword!!)
                        .addOnCompleteListener(this) { task ->
                            mProgressBar!!.hide()
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("tmt", "createUserWithEmail:success")
                                val userId = mAuth!!.currentUser!!.uid
                                // Verify Email
                                // verifyEmail();
                                // update user profile information
                                val currentUserDb = mDatabaseReference!!.child(userId).child("Infor")
                                currentUserDb.child("fullname").setValue(txtFullname)
                                currentUserDb.child("address").setValue(txtAddress)
                                currentUserDb.child("birthday").setValue(txtBirthday)
                                currentUserDb.child("numberphone").setValue(txtNumberphone)
                                currentUserDb.child("permission").setValue(intPermisstion)
                                currentUserDb.child("email").setValue(txtUsername)

                                val db = FirebaseFirestore.getInstance()
                                var mUser: User = User(userId, txtFullname.toString(), intPermisstion.toString()
                                        , txtNumberphone.toString(), txtAddress.toString(), txtUsername.toString(),
                                        txtBirthday.toString())
                                // Khởi tạo Root
                                db.collection(Parameter().root_User)
                                        .document(userId)
                                        .set(mUser)
                                        .addOnSuccessListener { documentReference ->
                                            updateUserInfoAndUI()
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("tmt err", exception.toString())
                                        }
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

