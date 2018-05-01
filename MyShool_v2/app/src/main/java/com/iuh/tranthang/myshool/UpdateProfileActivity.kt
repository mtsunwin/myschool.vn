package com.iuh.tranthang.myshool
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.ViewApdater.ProfileFragment
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_updateprofile.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Delayed
import kotlin.collections.HashMap


class UpdateProfileActivity : AppCompatActivity() {

    private var fullname: EditText? = null
    private var username: EditText? = null
    private var address: EditText? = null
    private var numberphone: EditText? = null
    private var birthday: EditText? = null
    private var txt_address: String = ""
    private var txt_phone: String = ""
    private var txt_email: String = ""
    private var txt_birthday: String = ""
    private var btnUpdate: Button? = null
    private var mProgressBar: ProgressDialog? = null

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null

    private var txtFullname: String? = ""
    private var txtUsername: String? = ""
    private var txtAddress: String? = ""
    private var txtNumberphone: String? = ""
    private var txtBirthday: String? = ""
    private var txtPermission: String? = ""
    private var intPermisstion: Int? = 0
    private var awesomeValidation: AwesomeValidation? = null
    private var imageView:ImageView?=null
    private var textCongviec: Boolean? = true
    private var textToCongTac: String? = ""
    private var textChucVu: String? = "Nhan vien"
    private var btnUpload: Button? = null
    private val PICK_IMAGE_REQUEST = 1234
    private var filePath: Uri? = null
    private var mUser:User?=null
    internal var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var mAuth: FirebaseAuth? = null
    private var dbFireStore:FirebaseFirestore?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updateprofile)
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation!!.addValidation(this, R.id.update_fullname, "([a-zA-Z' ]+){6,}", R.string.validation_address)
        awesomeValidation!!.addValidation(this, R.id.update_address, "([a-zA-Z' ]+){6,}", R.string.validation_address)
        awesomeValidation!!.addValidation(this, R.id.update_numberphone, "^[0-9]{9,}\$", R.string.validation_phone)
        update_initialise()
        btnUpload!!.setOnClickListener { view ->
            showFileChooser()
        }
        btnUpdate!!.setOnClickListener { view ->
            if(awesomeValidation!!.validate())
            updateAccount()
            else
                Toast.makeText(this,"nhập đúng định dạng để cập nhật",Toast.LENGTH_SHORT).show()
        }
        setText()
    }


    private fun update_initialise() {
        fullname = findViewById<View>(R.id.update_fullname) as EditText?
        address = findViewById<View>(R.id.update_address) as EditText?
        birthday = findViewById<View>(R.id.update_birthday) as EditText?
        numberphone = findViewById<View>(R.id.update_numberphone) as EditText?
        btnUpdate = findViewById<View>(R.id.btnUpdate) as Button?

//      upload file
        btnUpload = findViewById<Button>(R.id.update_btnUploadFile)
        imageView=findViewById(R.id.update_imageView)
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        mProgressBar = ProgressDialog(this)
        //btnUploadImage

        // Validation

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

    }

    //hàm upload file
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // hiển thị hình lên giao diện
    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Chon hinh"), PICK_IMAGE_REQUEST)
    }
    private fun setText() {
        var db = dbConnect()
        mAuth = FirebaseAuth.getInstance()
        if (db.isAuthentication()) {
            dbFireStore = FirebaseFirestore.getInstance()
            dbFireStore!!.collection(Parameter().root_User).document(mAuth!!.uid!!)
                    .get().addOnCompleteListener({ task ->
                if (task.isSuccessful) {
                    Log.e("Tmt inside", "mmmmmmmmmmmmmm")
                    var result: DocumentSnapshot = task.result
                    if (result.exists()) {
                        update_address.setText(result.data[Parameter().comp_address].toString())
                        update_birthday.setText(result.data[Parameter().comp_birthday].toString())
                        update_numberphone.setText(result.data[Parameter().comp_numberphone].toString())
                        update_fullname.setText(result.data[Parameter().comp_fullname].toString())
                    } else {
                        Log.e("tmt false", "false")
                    }
                }
            })
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

    private fun updateUserInfoAndUI() {
        /*     var intent: Intent = Intent(this@RegisterActivity, AdminActivity::class.java)
             Toast.makeText(this@RegisterActivity, R.string.created_success, Toast.LENGTH_LONG)
             startActivity(intent)*/
    }
    private fun updateAccount() {
        Log.e("UUID User:",dbConnect().getUid())

        var db2 = dbConnect()
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth!!.currentUser!!.uid
        mUser=User()
        if (db2.isAuthentication()) {
            dbFireStore = FirebaseFirestore.getInstance()
            dbFireStore!!.collection(Parameter().root_User).document(mAuth!!.uid!!)
                    .get().addOnCompleteListener({ task ->
                if (task.isSuccessful) {

                    Log.e("Tmt inside", "mmmmmmmmmmmmmm")
                    var result: DocumentSnapshot = task.result
                    result.data[Parameter().comp_address]
                    if(filePath!=null){
                        val progressDialog=ProgressDialog(this)
                        progressDialog.setTitle("Uploading...")
                        progressDialog.show()
                        var imageRef = storageReference!!.child("images/" + result.data[Parameter().comp_UId].toString())
                        Log.e("Image:",imageRef.path)
                        imageRef.putFile(filePath!!).addOnSuccessListener {
                            progressDialog.dismiss()
                        }
                                .addOnProgressListener { taskSnapshot ->
                                    val progress= 100.0* taskSnapshot.bytesTransferred/taskSnapshot.totalByteCount
                                    progressDialog.setMessage("Uploaded"+progress.toInt()+"")
                                }
                    }
                    val items= HashMap<String,Any>()
                    items.put("address",update_address.text.toString())
                    items.put("birthday",update_birthday.text.toString())
                    items.put("numberphone",update_numberphone.text.toString())
                    items.put("fullname",update_fullname.text.toString())
                    items.put("chucVu",result.data[Parameter().comp_chucVu].toString())
                    items.put("uid",result.data[Parameter().comp_UId].toString())
                    items.put("permission",result.data[Parameter().comp_Permission].toString())
                    items.put("email",result.data[Parameter().comp_email].toString())
                    items.put("heSoLuong",result.data[Parameter().comp_salary].toString())
                    if(filePath!=null)
                        items.put("url",result.data[Parameter().comp_UId].toString())
                    else
                        items.put("url",result.data[Parameter().comp_url].toString())
                    items.put("toCongTac",result.data[Parameter().comp_toCongTac].toString())
                    items.put("action",result.data[Parameter().comp_action].toString())
                    Log.e("Items",items.toString())
                    if (result.exists()) {
                        dbFireStore!!.collection(Parameter().root_User)
                                .document(userId).set(items).addOnSuccessListener {
                            Toast.makeText(this,"Successful update",Toast.LENGTH_SHORT).show()
                        }
                        Log.e("Update", "Successful")
                    }
                    } else {
                        Log.e("Update", "false")
                    }

            })

        }
        ProfileActivity().finish()
        this.finish()

    }
}