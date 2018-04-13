package com.iuh.tranthang.myshool

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {
    private var firebaseauth: FirebaseAuth?=null
    private var txtEmailForget: TextView?= null
    private var btnForgetPw: Button?=null
    private var btnBack: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgetpassword)
        firebaseauth= FirebaseAuth.getInstance()
        txtEmailForget=findViewById(R.id.usernameForgetPassword)
        btnForgetPw= findViewById(R.id.btn_forgetpassword)
        btnBack=findViewById(R.id.btn_BackLogin)
        btnForgetPw!!.setOnClickListener {
            resetPassword()
        }
        btnBack!!.setOnClickListener {
            finish()
        }
    }
    fun resetPassword(){
        var email: String?=null
        email = txtEmailForget!!.text.toString()
        if(email.equals("")){
            Log.e("Null","Lỗi null cmnr")
            Toast.makeText(this,"Chưa nhập Email",Toast.LENGTH_LONG)
        }
        else{
            firebaseauth!!.sendPasswordResetEmail(email!!).addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    Log.e("success","successful cmnr")
                    Toast.makeText(this, "Đã gửi yêu cầu reset password", Toast.LENGTH_SHORT).show()

                }
                else{
                    Toast.makeText(this,"Xảy ra lỗi khi gửi yêu cầu resetpassword",Toast.LENGTH_SHORT).show()
                    Log.e("Fail","Lỗi cmnr")
                }
            }
        }

    }
}
