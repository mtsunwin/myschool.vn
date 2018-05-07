package com.iuh.tranthang.myshool.ViewApdater

import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.mUser
import kotlinx.android.synthetic.main.layout_item_list_user.view.*
import kotlinx.android.synthetic.main.layout_item_list_user_updatesalary.view.*
import java.io.File
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


/**
 * Created by ThinkPad on 4/19/2018.
 */

class RecycleViewUserAdapter(private val items: ArrayList<mUser>, val actionCall: (mUser) -> Unit)
    : RecyclerView.Adapter<RecycleViewUserAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items.get(position), actionCall)
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

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item_list_user, parent, false)) {
        internal var storage: FirebaseStorage? = null
        private var storageReference: StorageReference? = null
        private var txt_url: String? = null
        private var txt_phone: String? = null
        private var intent_call: Intent? = null
        private var choose: Intent? = null

        fun bind(mU: mUser, actionCall: (mUser) -> Unit) = with(itemView) {
            val listStringPermission = context.resources.getStringArray(R.array.select_permission)
            storage = FirebaseStorage.getInstance()
            storageReference = storage!!.reference
            txt_fullname.text = mU.getFullname()

            txt_url = mU.getUrl()
            when (mU.getPermission()) {
                "0" -> txt_chucvu.text = "Kế toán"
                "1" -> txt_chucvu.text = "Giáo viên"
                "2" -> txt_chucvu.text = "Nhân viên"
                "3" -> txt_chucvu.text = "Admin"
                else -> txt_chucvu.text = ""
            }
            if (mU.getNumberphone().length > 0) {
                btn_Call.visibility = visibility
                txt_phone = mU.getNumberphone()
                Log.e("phone", txt_phone.toString())
            }
            btn_Call.setOnClickListener {
                //                if (phone.length > 0) {
//                btn_Call.visibility = visibility
//                    txt_phone=phone.toString()
//                    Log.e("txt_phone",txt_phone.toString())
//                    intent_call= Intent(Intent.ACTION_CALL)
//                    intent_call!!.setData(Uri.parse(txt_phone.toString()))
//                    context.startActivity(intent_call)
//                }
                actionCall(mU)
            }
            if (txt_url!!.length > 0) {
                try {
                    val tmpFile = File.createTempFile("img", "png")
                    val reference = FirebaseStorage.getInstance().getReference("images/")

                    //  "id" is name of the image file....

                    reference.child(txt_url.toString()).getFile(tmpFile).addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                        val image = BitmapFactory.decodeFile(tmpFile.getAbsolutePath())
                        img_avatar!!.setImageBitmap(image)
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
