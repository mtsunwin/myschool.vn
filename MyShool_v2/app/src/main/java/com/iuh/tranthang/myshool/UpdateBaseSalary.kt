package com.iuh.tranthang.myshool

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.model.Parameter
import kotlinx.android.synthetic.*

/**
 * Created by Administrator on 4/27/2018.
 */
class UpdateBaseSalary: DialogFragment() {

   /* override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val db = FirebaseFirestore.getInstance()
        val alertdialogBuilder= AlertDialog.Builder(activity)
        val dialog= alertdialogBuilder.create()

        //val dialogView= layoutInflater.inflate(R.layout.fragment_dialogupdatebasesalary,null)
        var txtHeSoLuongcoban:EditText?=null
//        txtHeSoLuongcoban= clearFindViewByIdCache(R.id.txtHeSoLuongCoBan)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setTitle("Cập nhật lương cơ bản")
        dialog.setPositiveButton("Xác nhận", { dialogInterface: DialogInterface, i: Int -> })
        val customDialog = dialog.create()
        val luongID: String? = ""
        customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener({
            if (txtHeSoLuongcoban!!.length() > 0) {
                val items = HashMap<String, Any>()
                items.put("LuongCoBan", txtHeSoLuongcoban!!.text.toString())
                db.collection(Parameter().root_Luong)
                        .document("Salary").set(items)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(this, "Successful update", Toast.LENGTH_SHORT).show()
                        }
                Log.e("Update", "Successful")
            }
        })
    }
    override fun dismiss() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/



}