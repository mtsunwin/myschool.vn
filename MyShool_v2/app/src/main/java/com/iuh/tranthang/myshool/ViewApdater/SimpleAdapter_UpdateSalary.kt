package com.iuh.tranthang.myshool.ViewApdater

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.mUser
import kotlinx.android.synthetic.main.layout_item_list_user_updatesalary.view.*
import java.io.File


/**
 * Created by ThinkPad on 4/19/2018.
 */

class SimpleAdapter_UpdateSalary(private val items: ArrayList<mUser>) : RecyclerView.Adapter<SimpleAdapter_UpdateSalary.VH_updatesalary>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH_updatesalary {
        return VH_updatesalary(parent)
    }

    override fun onBindViewHolder(holder: VH_updatesalary?, position: Int) {
        holder!!.bind(items[position].getUid(), items[position].getFullname(), items[position].getPermission(), items[position].getNumberphone(),
                items[position].getAddress(), items[position].getEmail(), items[position].getBirthday(), items[position].getToCongTac(),
                items[position].getChucVu(), items[position].getUrl(), items[position].isAction(), items[position].getCoefficient(), items[position].getIdDevice())
    }

    override fun getItemCount(): Int = items.size

    fun addItem(name: String) {
//        items.add(name)
        notifyItemInserted(items.size)
    }

    fun removeAt(position: Int) {
        Log.e("tmt at", position.toString())
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    class VH_updatesalary(parent: ViewGroup) : RecyclerView.ViewHolder(


            LayoutInflater.from(parent.context).inflate(R.layout.layout_item_list_user_updatesalary, parent, false)) {
        internal var storage: FirebaseStorage? = null
        private var storageReference: StorageReference? = null

        private var mAuth: FirebaseAuth? = null
        private var dbFireStore: FirebaseFirestore? = null
        private var mUser: mUser? = null
        private lateinit var btn: Button
        private var txt_uuid: String? = null
        private var txt_name: String? = null
        private var txt_phone: String? = null
        private var txt_email: String? = null
        private var txt_birthday: String? = null
        private var txt_toCongtac: String? = null
        private var txt_chucVu: String? = null
        private var txt_url: String? = null
        private var txt_action: Boolean? = null
        private var txt_luongCoBan: String? = null
        private var txt_iddevice: String? = null
        private var txt_address: String? = null
        private var txt_permission: String? = null


        fun bind(uid: String, name: String, permission: String, phone: String, address: String, email: String, birhday: String, toCongTac: String, chucVu: String, url: String, action: Boolean, luongCoBan: String, iddevice: String) = with(itemView) {
            txt_fullname_updateSalary.text = name
            txt_chucvu_updateSalary.text = permission
            storage = FirebaseStorage.getInstance()
            storageReference = storage!!.reference
            txt_uuid = uid.toString()
            txt_name = name.toString()
            txt_phone = phone.toString()
            txt_email = email.toString()
            txt_birthday = birhday.toString()
            txt_toCongtac = toCongTac.toString()
            txt_chucVu = chucVu.toString()
            txt_url = url.toString()
            txt_action = action
            txt_address = address.toString()
            txt_luongCoBan = luongCoBan.toString()
            txt_iddevice = iddevice.toString()
            txt_permission = permission.toString()
            when (permission) {
                "0" -> txt_chucvu_updateSalary.text = "Kế toán"
                "1" -> txt_chucvu_updateSalary.text = "Giáo viên"
                "2" -> txt_chucvu_updateSalary.text = "Nhân viên"
                "3" -> txt_chucvu_updateSalary.text = "Admin"
                else -> txt_chucvu_updateSalary.text = ""
            }
            if (txt_url!!.length > 0) {
                try {
                    val tmpFile = File.createTempFile("img", "png")
                    val reference = FirebaseStorage.getInstance().getReference("images/")

                    //  "id" is name of the image file....

                    reference.child(txt_url.toString()).getFile(tmpFile).addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                        val image = BitmapFactory.decodeFile(tmpFile.getAbsolutePath())
                        img_avatar_updateSalary!!.setImageBitmap(image)
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            btn_updateSalary.setOnClickListener { v ->
                var builder: AlertDialog.Builder = AlertDialog.Builder(context)
//                var view: View =LayoutInflater.from(context).inflate(R.layout.layout_dialogupdatesalary,null)
                var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                var view: View = inflater.inflate(R.layout.layout_dialogupdatesalary, null)
                builder.setView(view)
                builder.setTitle("Cập nhật hệ số lương")
                var txtHeSoLuongCu: TextView? = null
                var EditTextHeSoLuongMoi: EditText? = null
                txtHeSoLuongCu = view.findViewById(R.id.txtHesoluongCu_updateSalary)
                EditTextHeSoLuongMoi = view.findViewById(R.id.txtHeSoLuongMoi_update)
                Log.e("txt_luongCoBan", txt_luongCoBan.toString())
                if (!txt_luongCoBan!!.equals("abc") || txt_luongCoBan!!.length > 0) txtHeSoLuongCu.setText(txt_luongCoBan.toString())
                else txtHeSoLuongCu.setText("Chưa thiết lập hệ số lương")
                builder.setNegativeButton(R.string.dialogAsk_no, object : DialogInterface.OnClickListener { // cancel
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0!!.dismiss()
                    }
                })
                builder.setPositiveButton(R.string.dialogAsk_yes, object : DialogInterface.OnClickListener { // apply
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        var txt_HeSoLuongMoi: String? = ""
                        txt_HeSoLuongMoi = EditTextHeSoLuongMoi!!.editableText.toString()
                        Log.e("txt_HeSoLuongMoi", EditTextHeSoLuongMoi.editableText.toString())
                        if (txt_HeSoLuongMoi!!.length > 0) {
                            txt_luongCoBan = EditTextHeSoLuongMoi.text.toString()
                            var dbFireStore = FirebaseFirestore.getInstance()
                            dbFireStore.collection(Parameter.root_User)
                            Log.e("tmt id", txt_uuid)
                            // Thực hiện hiện update
                            var washingtonRef: DocumentReference =
                                    dbFireStore.collection(Parameter.root_User).document(txt_uuid!!)
                            washingtonRef.update(Parameter.comp_baseSalary, txt_luongCoBan.toString()).addOnSuccessListener { void ->
                                //updateSalary(txt_uuid!!,txt_name!!,txt_permission!!,txt_phone!!,txt_address!!,txt_email!!,txt_birthday!!,txt_toCongtac!!,txt_chucVu!!,txt_url!!,txt_action!!,txt_luongCoBan!!,txt_iddevice!!)
                            }.addOnFailureListener { exception ->
                                Log.e("tmt", "that bai")
                            }
                        }

                    }

                })
                var dialog: Dialog = builder.create()
                dialog.show()
            }
        }

        private fun updateSalary(uid: String, name: String,
                                 permission: String, phone: String,
                                 address: String, email: String, birhday: String,
                                 toCongtac: String, chucVu: String, url: String,
                                 action: Boolean, luongCoBan: String, iddevice: String) {
            var db2 = dbConnect()
            mAuth = FirebaseAuth.getInstance()
            val userId = mAuth!!.currentUser!!.uid
            mUser = mUser()
            if (db2.isAuthentication()) {
                dbFireStore = FirebaseFirestore.getInstance()
                dbFireStore!!.collection(Parameter.root_User).document(mAuth!!.uid!!)
                        .get().addOnCompleteListener({ task ->
                            if (task.isSuccessful) {
                                var result: DocumentSnapshot = task.result
                                val items = HashMap<String, Any>()
                                items.put("address", address.toString())
                                items.put("birthday", birhday.toString())
                                items.put("numberphone", phone.toString())
                                items.put("fullname", name.toString())
                                items.put("chucVu", chucVu.toString())
                                items.put("uid", uid.toString())
                                items.put("permission", permission.toString())
                                items.put("email", email.toString())
                                items.put("heSoLuong", result.data[Parameter.comp_baseSalary].toString())
                                items.put("url", result.data[Parameter.comp_url].toString())
                                items.put("toCongTac", result.data[Parameter.comp_toCongTac].toString())
                                items.put("action", result.data[Parameter.comp_action].toString())
                                Log.e("Items", items.toString())
                                if (result.exists()) {
                                    dbFireStore!!.collection(Parameter.root_User)
                                            .document(userId).set(items).addOnSuccessListener {

                                            }
                                    Log.e("Update", "Successful")
                                }
                            } else {
                                Log.e("Update", "false")
                            }
                        })
            }
        }
    }


}
