package com.iuh.tranthang.myshool

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.model.Parameter
import kotlinx.android.synthetic.*

/**
 * Created by Administrator on 4/27/2018.
 */
class UpdateBaseSalary : AppCompatActivity() {
    private var txtSalary:EditText?=null
    private var txtSalary_old:TextView?=null
    private var btnXacNhan:Button?=null
    private var txtHeSoLuongcoban:String?=""
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        val db = FirebaseFirestore.getInstance()
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.fragment_dialogupdatebasesalary)
        txtSalary= findViewById(R.id.txtHeSoLuongCoBan)
        txtSalary_old=findViewById(R.id.txtHeSoLuongCoBanOld)
        btnXacNhan=findViewById(R.id.btnXacNhanUpdateBaseSalary)
        btnXacNhan!!.setOnClickListener{
            txtHeSoLuongcoban=txtSalary!!.text.toString()
            Log.e("Salary",txtSalary!!.text.toString())
            if (txtHeSoLuongcoban!!.length > 0) {
                val items = HashMap<String, Any>()
                items.put("LuongCoBan", txtHeSoLuongcoban.toString())
                db.collection(Parameter().root_Luong)
                        .document("Salary").set(items)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(this, "Successful update", Toast.LENGTH_SHORT).show()
                        }
                Log.e("Update", "Successful")
            }
        }
    }
}