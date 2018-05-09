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
import java.io.File


/**
 * Created by ThinkPad on 4/19/2018.
 */

class RecycleViewUserAdapter(private val items: ArrayList<mUser>, val actionCall: (mUser) -> Unit,
                             val actionClick: (mUser) -> Unit)
    : RecyclerView.Adapter<RecycleViewUserAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items.get(position), actionCall, actionClick)
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

        fun bind(mU: mUser, actionCall: (mUser) -> Unit, actionClick: (mUser) -> Unit) = with(itemView) {
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
                actionCall(mU)
            }
            card_view_user.setOnClickListener {
                actionClick(mU)
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
