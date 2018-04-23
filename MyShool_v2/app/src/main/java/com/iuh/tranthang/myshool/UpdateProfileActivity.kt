package com.iuh.tranthang.myshool


import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_updateprofile.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updateprofile)
        update_initialise()
        Log.e("CongViecCreate:......", textCongviec.toString())
        val db = FirebaseFirestore.getInstance()
        setText()
    }


    private fun update_initialise() {
        fullname = findViewById<View>(R.id.update_fullname) as EditText?
        btnUpdate = findViewById<View>(R.id.btnUpdate) as Button?
        address = findViewById<View>(R.id.update_address) as EditText?
        numberphone = findViewById<View>(R.id.update_numberphone) as EditText?
        birthday = findViewById<View>(R.id.update_birthday) as EditText?
//      upload file
        btnUpload = findViewById<Button>(R.id.update_btnUploadFile)
        imageView=findViewById(R.id.update_imageView)
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        mProgressBar = ProgressDialog(this)
        //btnUploadImage
        btnUpload!!.setOnClickListener { view ->
            showFileChooser()
        }
        btnUpdate!!.setOnClickListener { view ->
            updateAccount()
        }
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
    private fun setText(){
        var token=getSharedPreferences("username", Context.MODE_PRIVATE)
        txt_address= ProfileActivity().frm_address.toString()
        txt_birthday=ProfileActivity().frm_birthday.toString()
        txt_phone=ProfileActivity().frm_phone.toString()
        update_address.setText(txtAddress.toString())
        update_birthday.setText(txtBirthday.toString())
        update_numberphone.setText(txt_phone.toString())
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

    /*  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
          super.onActivityResult(requestCode, resultCode, data)
          if(requestCode==PICK_IMAGE_REQUEST && resultCode== Activity.RESULT_OK && data!=null &&data.data!=null){
              filePath=data.data
              try{
                  val bitmap == Medi
              }catch ()
          }
      }*/
    private fun updateUserInfoAndUI() {
   /*     var intent: Intent = Intent(this@RegisterActivity, AdminActivity::class.java)
        Toast.makeText(this@RegisterActivity, R.string.created_success, Toast.LENGTH_LONG)
        startActivity(intent)*/
    }
    private fun updateAccount() {
        mAuth=FirebaseAuth.getInstance()
        var token = getSharedPreferences("usename", Context.MODE_PRIVATE)
        val token_ps = getSharedPreferences("permission", Context.MODE_PRIVATE)
        Log.e("permission user",token_ps.getString("permission",""))
        txtFullname = fullname?.text.toString()
        txtUsername = username?.text.toString()
        txtAddress = address?.text.toString()
        txtBirthday = birthday?.text.toString()
        txtNumberphone = numberphone?.text.toString()
        Log.e("UUID User:",dbConnect().getUid())

        if (intPermisstion == 0) {
            mUser = User(dbConnect().getUid(), txtFullname.toString(), token_ps.getString("permission","")
                    , txtNumberphone.toString(), txtAddress.toString(), txtUsername.toString(),
                    txtBirthday.toString(), textToCongTac.toString(), textChucVu.toString(),"")
        }
        else mUser = User(dbConnect().getUid(), txtFullname.toString(), token_ps.getString("permission","")
                        , txtNumberphone.toString(), txtAddress.toString(), token.getString("loginusername",""),txtBirthday.toString(),"","","")
        }




}
