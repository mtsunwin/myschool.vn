package com.iuh.tranthang.myshool


import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.mUser
import java.text.SimpleDateFormat
import java.util.*


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
    private var dbFireStore: FirebaseFirestore? = null

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
    private var txtErrorPassword: TextView? = null
    private var awesomeValidation: AwesomeValidation? = null
    private var textCongviec: Boolean? = true
    private var textToCongTac: String? = ""
    private var textChucVu: String? = "Nhan vien"
    private var mMUser: mUser? = null
    internal var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var txtSelectUser: TextView? = null
    private var txtSelectUser_1: TextView? = null
    private var txtusername: String? = null
    private var txtpassword: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initialise()
    }

    private fun initialise() {
        val db = FirebaseFirestore.getInstance()
        fullname = findViewById<View>(R.id.fullname) as EditText?
        username = findViewById<View>(R.id.username) as EditText?
        password = findViewById<View>(R.id.password) as EditText?
        btnLogin = findViewById<View>(R.id.btn_rRegister) as Button?
        address = findViewById<View>(R.id.address) as EditText?
        numberphone = findViewById<View>(R.id.numberphone) as EditText?
        birthday = findViewById<View>(R.id.birthday) as EditText?
        txtSelectUser = findViewById<View>(R.id.selectPerTeacher_1) as TextView?
        txtSelectUser_1 = findViewById<View>(R.id.selectPerTeacherLead_1) as TextView?

        txtSelectUser!!.visibility = View.GONE
        txtSelectUser_1!!.visibility = View.GONE

//      upload file
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        spinnerPermisstion = findViewById<View>(R.id.selectPermission) as Spinner?
        spinnerPermisstion!!.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.select_permission))
        spinnerPermisstion!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem = p0!!.getItemAtPosition(p2).toString()
                if (selectedItem.equals("Giáo viên")) {
                    spinnerPerTeacher!!.visibility = View.VISIBLE
                    spinnerPerTeacherLead!!.visibility = View.VISIBLE
                    txtSelectUser!!.visibility = View.VISIBLE
                    txtSelectUser_1!!.visibility = View.VISIBLE
                } else {
                    spinnerPerTeacher!!.visibility = View.GONE
                    spinnerPerTeacherLead!!.visibility = View.GONE
                    txtSelectUser!!.visibility = View.GONE
                    txtSelectUser_1!!.visibility = View.GONE
                }
                if (!selectedItem.equals("Công việc..")) {
                    textCongviec = false
                    Log.e("CongViecOnFalse:..", textCongviec.toString())
                } else {
                    textCongviec = true
                    Log.e("CongViecOnTrue", textCongviec.toString())
                }

            }
        }
        spinnerPerTeacher = findViewById<View>(R.id.selectPerTeacher) as Spinner?
        spinnerPerTeacher!!.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.select_permission_teacher))
        spinnerPerTeacher!!.visibility = View.GONE
        spinnerPerTeacher!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem = p0!!.getItemAtPosition(p2).toString()
                if (selectedItem.equals("Tổ công tác.."))
                    textToCongTac = " "
                else
                    textToCongTac = selectedItem
            }
        }
        spinnerPerTeacherLead = findViewById<View>(R.id.selectPerTeacherLead) as Spinner?
        spinnerPerTeacherLead!!.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.select_permission_teacher_leader))
        spinnerPerTeacherLead!!.visibility = View.GONE
        spinnerPerTeacherLead!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem = p0!!.getItemAtPosition(p2).toString()
                textChucVu = selectedItem
            }
        }
        mProgressBar = ProgressDialog(this)
        // Validation
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation!!.addValidation(this, R.id.fullname, "([a-zA-Z' ]+){6,}", R.string.validation_fullname)
        awesomeValidation!!.addValidation(this, R.id.password, "^[A-Za-z0-9]{6,}\$", R.string.validation_number)
        awesomeValidation!!.addValidation(this, R.id.address, "^([a-zA-Z0-9' ]+){6,}", R.string.validation_address)
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
        }
        password!!.setOnClickListener {
            txtErrorPassword!!.setText("")
        }

        btnLogin!!.setOnClickListener { view ->
            Log.e("CongViec:............", textCongviec.toString())
            if (awesomeValidation!!.validate()) {
                if (password!!.text.length < 6)
                    txtErrorPassword!!.setText("Password characters must be more than 6")
                if (!Patterns.EMAIL_ADDRESS.matcher(username!!.text.toString()).matches()) {
                } else if (textCongviec == true)
                    Toast.makeText(this, "Chọn công việc", Toast.LENGTH_SHORT).show()
                else createNewAccount()
            }
        }
    }

    //upload hinh len firebase
    private fun upload() {
        /*   if(filePath!=null){
           val progressDialog=ProgressDialog(this)
           progressDialog.setTitle("Uploading...")
           progressDialog.show()
           val imageRef= storageReference!!.child("images/"+UUID.randomUUID().toString())
           Log.e("Image:",imageRef.path)
           imageRef.putFile(filePath!!).addOnSuccessListener {
               progressDialog.dismiss()
               Toast.makeText(applicationContext,"KHONG THANH CONG UP HINH",Toast.LENGTH_SHORT).show()

           }
                   .addOnProgressListener { taskSnapshot ->
                       val progress= 100.0* taskSnapshot.bytesTransferred/taskSnapshot.totalByteCount
                       progressDialog.setMessage("Uploaded"+progress.toInt()+"")
                   }
       }*/
    }

    private fun createNewAccount() {
        val db = FirebaseFirestore.getInstance()
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
                            var mMUser: mUser
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("tmt", "createUserWithEmail:success")
                                val userId = mAuth!!.currentUser!!.uid
                                // update user profile information

                                if (intPermisstion == 1) {
                                    mMUser = mUser(userId, txtFullname.toString(), intPermisstion.toString()
                                            , txtNumberphone.toString(), txtAddress.toString(), txtUsername.toString(),
                                            txtBirthday.toString(), textToCongTac.toString(), textChucVu.toString(), "",
                                            true, "", "")
                                } else mMUser = mUser(userId, txtFullname.toString(), intPermisstion.toString()
                                        , txtNumberphone.toString(), txtAddress.toString(), txtUsername.toString(),
                                        txtBirthday.toString(), "", "", "", true, "", "")

                                // Khởi tạo Root
                                db.collection(Parameter.root_User)
                                        .document(userId).set(mMUser)
                                        .addOnSuccessListener {
                                            finish()
                                            var token = getSharedPreferences("username", Context.MODE_PRIVATE)
                                            var token_pw = getSharedPreferences("password", Context.MODE_PRIVATE)
                                            txtusername = token!!.getString("loginusername", " ")
                                            txtpassword = token_pw!!.getString("password", " ")
                                            Log.e("username +password", txtusername + "--" + txtpassword)
                                            mAuth!!.signOut()
                                            mAuth!!.signInWithEmailAndPassword(txtusername!!, txtpassword!!)
                                            Toast.makeText(this, R.string.registerCompleted, Toast.LENGTH_SHORT).show()
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
}

