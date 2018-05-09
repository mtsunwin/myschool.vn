package com.iuh.tranthang.myshool

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.model.Parameter
import kotlinx.android.synthetic.main.activity_updateprofile.*
import kotlinx.android.synthetic.main.fragment_dialogupdatebasesalary.*

/**
 * Created by Administrator on 4/27/2018.
 */
class UpdateBaseSalary : AppCompatActivity() {
    private var txtSalary: EditText? = null
    private var txtSalary_old: TextView? = null
    private var btnXacNhan: Button? = null
    private var txtHeSoLuongcoban: String? = ""

    private var dbFireStore:FirebaseFirestore?=null
    private var txtHeSoLuongCoBanOld:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dialogupdatebasesalary)
        val db = FirebaseFirestore.getInstance()
        txtSalary = findViewById(R.id.txtHeSoLuongCoBan)
        txtSalary_old = findViewById(R.id.txtHeSoLuongCoBanOld)
        btnXacNhan = findViewById(R.id.btnXacNhanUpdateBaseSalary)
        txtHeSoLuongCoBanOld= findViewById(R.id.txtHeSoLuongCoBanOld)
        dbFireStore = FirebaseFirestore.getInstance()
        dbFireStore!!.collection(Parameter.root_Luong).document("Salary")
                    .get().addOnCompleteListener({ task ->
                if (task.isSuccessful) {
                    Log.e("Tmt inside", "mmmmmmmmmmmmmm")
                    var result: DocumentSnapshot = task.result
                    if (result.exists()) {
                        txtHeSoLuongCoBanOld!!.text=result.data["LuongCoBan"].toString()
                    } else {
                        Log.e("tmt false", "false")
                    }
                }
            })

        btnXacNhan!!.setOnClickListener {
            txtHeSoLuongcoban = txtSalary!!.text.toString()
            Log.e("Salary", txtSalary!!.text.toString())
            if (txtHeSoLuongcoban!!.length > 0) {
                val items = HashMap<String, Any>()
                items.put("LuongCoBan", txtHeSoLuongcoban.toString())
                db.collection(Parameter.root_Luong)
                        .document("Salary").set(items)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(this, "Cập nhật lương cơ bản thành công", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                Log.e("Update", "Successful")
            }
        }   
    }
}