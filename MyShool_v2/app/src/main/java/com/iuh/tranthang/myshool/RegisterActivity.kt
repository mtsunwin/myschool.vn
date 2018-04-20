package com.iuh.tranthang.myshool


import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User
import java.text.SimpleDateFormat
import java.util.*
import android.widget.TextView



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
    private var spinnerPerTeacher: Spinner? = null
    private var spinnerPerTeacherLead: Spinner? = null
    private var intPermisstion: Int? = 0
    private var txtErrorUserName: TextView? = null
    private var txtErrorPassword: TextView? = null
    private var awesomeValidation: AwesomeValidation? = null
    private var textCongviec:Boolean?=true
    private var textToCongTac:String?=""
    private var textChucVu:String?="Nhan vien"
    private var mUser: User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initialise()
        Log.e("CongViecCreate:......",textCongviec.toString())
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
        spinnerPermisstion!!.onItemSelectedListener= object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem= p0!!.getItemAtPosition(p2).toString()
                if(selectedItem.equals("Giáo viên")){
                    spinnerPerTeacher!!.visibility=View.VISIBLE
                    spinnerPerTeacherLead!!.visibility=View.VISIBLE
                }
                else{
                    spinnerPerTeacher!!.visibility=View.GONE
                    spinnerPerTeacherLead!!.visibility=View.GONE
                }
                if (!selectedItem.equals("Công việc..")){
                    textCongviec=false
                    Log.e("CongViecOnFalse:..",textCongviec.toString())
                }
                else{
                    textCongviec=true
                    Log.e("CongViecOnTrue",textCongviec.toString())
                }

        }
        }
        spinnerPerTeacher = findViewById<View>(R.id.selectPerTeacher) as Spinner?
        spinnerPerTeacher!!.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.select_permission_teacher))
        spinnerPerTeacher!!.visibility = View.GONE
        spinnerPerTeacher!!.onItemSelectedListener= object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem= p0!!.getItemAtPosition(p2).toString()
                if(selectedItem.equals("Tổ công tác.."))
                    textToCongTac=""
                else
                    textToCongTac=selectedItem
            }
        }
        spinnerPerTeacherLead = findViewById<View>(R.id.selectPerTeacherLead) as Spinner?
        spinnerPerTeacherLead!!.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.select_permission_teacher_leader))
        spinnerPerTeacherLead!!.visibility = View.GONE
        spinnerPerTeacherLead!!.onItemSelectedListener= object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem= p0!!.getItemAtPosition(p2).toString()
                textChucVu=selectedItem

            }
        }
        mProgressBar = ProgressDialog(this)

        // Validation
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation!!.addValidation(this, R.id.fullname, "^[A-Za-z\\s\\u0080-\\u9fff]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.validation_number)
        awesomeValidation!!.addValidation(this, R.id.password, "^[A-Za-z0-9]{6,}\$", R.string.validation_number)
        awesomeValidation!!.addValidation(this, R.id.address, "^[A-Za-z0-9]{6,}\$", R.string.validation_number)
        awesomeValidation!!.addValidation(this, R.id.numberphone, "^[0-9]{9,}\$", R.string.validation_phone)
//        awesomeValidation!!.addValidation(this, R.id.selectPermission, "!=", R.string.validation_phone)
        // POP DATE
        var calendar = Calendar.getInstance()
        val DateFragment = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month + 1)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val dateFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(dateFormat, Locale.US)
            birthday!!.setText(sdf.format(calendar.time))
        }

        birthday!!.setOnClickListener { view ->
            Log.e("tmt check", "oke roi")
            DatePickerDialog(this, DateFragment,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

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
            Log.e("CongViec:............",textCongviec.toString())
            if (awesomeValidation!!.validate()) {
                if (password!!.text.length < 6)
                    txtErrorPassword!!.setText("Password characters must be more than 6")
                if (!Patterns.EMAIL_ADDRESS.matcher(username!!.text.toString()).matches()) {
                    txtErrorUserName!!.setText("Wrong format email.")
                }
                if (txtErrorUserName!!.text.length > 0 || txtErrorPassword!!.text.length > 0)
                    Toast.makeText(this, "Input complete username and password", Toast.LENGTH_SHORT).show()
                else if(textCongviec==true)
                    Toast.makeText(this,"Chọn công việc",Toast.LENGTH_SHORT).show()
                 else   createNewAccount()
            }
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
                            var mUser:User
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
                                currentUserDb.child("toCongTac").setValue(textToCongTac)
                                currentUserDb.child("ChucVu").setValue(textChucVu)
                                Log.e("User:",userId+"-"+txtFullname.toString()+"-"+ intPermisstion.toString()
                                        +"-"+ txtNumberphone.toString()+"-"+ txtAddress.toString()+"-"+ txtUsername.toString()+"-"+
                                        txtBirthday.toString()+"-"+textToCongTac.toString()+"-"+textChucVu.toString())
                                val db = FirebaseFirestore.getInstance()
                                if(intPermisstion==1){
                                    mUser= User(userId, txtFullname.toString(), intPermisstion.toString()
                                            , txtNumberphone.toString(), txtAddress.toString(), txtUsername.toString(),
                                            txtBirthday.toString(),textToCongTac.toString(),textChucVu.toString())
                                }
                               else mUser = User(userId, txtFullname.toString(), intPermisstion.toString()
                                       , txtNumberphone.toString(), txtAddress.toString(), txtUsername.toString(),
                                       txtBirthday.toString(), "", "")
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

