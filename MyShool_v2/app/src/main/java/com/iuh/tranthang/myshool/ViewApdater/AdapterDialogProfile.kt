package com.iuh.tranthang.myshool.ViewApdater


import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.mUser
import java.io.File

/**
 * Created by ThinkPad on 5/9/2018.
 */
class AdapterDialogProfile : DialogFragment() {
    public val key_Name = "1"
    public val key_Permisstion = "2"
    public val key_Birthday = "3"
    public val key_Address = "4"
    public val key_NumberPhone = "5"
    public val key_Salary = "6"
    public val key_Email = "7"
    public val key_Avatar = "7"

    private lateinit var txtFullName: TextView
    private lateinit var txtPermisstion: TextView
    private lateinit var txtBirthday: TextView
    private lateinit var txtAddress: TextView
    private lateinit var txtNumberPhone: TextView
    private lateinit var txtSalary: TextView
    private lateinit var txtEmail: TextView
    private lateinit var ImageLg: ImageView

    //Được dùng khi khởi tạo dialog mục đích nhận giá trị
    fun newInstance(mU: mUser): AdapterDialogProfile {
        var dialogAsk: AdapterDialogProfile = AdapterDialogProfile()
        val arg: Bundle = Bundle()
        arg.putString(key_Name, mU.getFullname())
        arg.putString(key_Permisstion, mU.getToCongTac())
        arg.putString(key_Birthday, mU.getBirthday())
        arg.putString(key_Address, mU.getAddress())
        arg.putString(key_NumberPhone, mU.getNumberphone())
        arg.putString(key_Salary, mU.getCoefficient())
        arg.putString(key_Email, mU.getEmail())
        arg.putString(key_Avatar, mU.getUrl())

        dialogAsk.arguments = arg
        return dialogAsk
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.layout_profile_pop, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDialog().getWindow().requestFeature(android.support.v4.app.DialogFragment.STYLE_NO_TITLE); // Tắt title của dialog

        txtFullName = view.findViewById(R.id.txt_nickname_pop)
        txtBirthday = view.findViewById(R.id.frm_txtBirthday_pop)
        txtAddress = view.findViewById(R.id.frm_txtAddress_pop)
        txtNumberPhone = view.findViewById(R.id.frm_txtNumPhone_pop)
        txtPermisstion = view.findViewById(R.id.txt_chucvu_pop)
        txtSalary = view.findViewById(R.id.frm_txtLuong_pop)
        txtEmail = view.findViewById(R.id.frm_txtEmail_pop)
        ImageLg = view.findViewById(R.id.imgAvatar_pop)

        var lenar_address: LinearLayout = view.findViewById(R.id.linear_address)
        var lenar_NumberPhone: LinearLayout = view.findViewById(R.id.linear_numberphone)
        var lenar_birthday: LinearLayout = view.findViewById(R.id.linear_birthday)
        var lenar_Salary: LinearLayout = view.findViewById(R.id.linear_salary)
        var lenar_Email: LinearLayout = view.findViewById(R.id.linear_email)

        if (arguments.getString(key_Email).length == 0) {
            lenar_Email.visibility = View.GONE
        }
        if (arguments.getString(key_Salary).length == 0) {
            lenar_Salary.visibility = View.GONE
        }
        if (arguments.getString(key_Birthday).length == 0) {
            lenar_birthday.visibility = View.GONE
        }
        if (arguments.getString(key_NumberPhone).length == 0) {
            lenar_NumberPhone.visibility = View.GONE
        }
        if (arguments.getString(key_Address).length == 0) {
            lenar_address.visibility = View.GONE
        }

        txtFullName.text = arguments.getString(key_Name)
        txtBirthday.text = arguments.getString(key_Birthday)
        txtAddress.text = arguments.getString(key_Address)
        txtNumberPhone.text = arguments.getString(key_NumberPhone)
        txtPermisstion.text = arguments.getString(key_Permisstion)
        txtSalary.text = arguments.getString(key_Salary)
        txtEmail.text = arguments.getString(key_Email)

        if (arguments.getString(key_Avatar).length > 0) {
            try {
                val tmpFile = File.createTempFile("img", "png")
                val reference = FirebaseStorage.getInstance().getReference("images/")
                reference.child(arguments.getString(key_Avatar)).getFile(tmpFile)
                        .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                            val image = BitmapFactory.decodeFile(tmpFile.getAbsolutePath())
                            ImageLg!!.setImageBitmap(image)
                        })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
//        btnApply = view.findViewById(R.id.btnDialog_apply)
//        btnCancel = view.findViewById(R.id.btnDialog_cancel)
//        btnApply.setOnClickListener { view ->
//            var send: sendReponse = activity as sendReponse
//            send.completed(true)
//            this.dismiss()
//        }
//        btnCancel.setOnClickListener { view ->
//            super.onDismiss(dialog)
//            var send: sendReponse = activity as sendReponse
//            send.completed(false)
//            this.dismiss()
//        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
//        var send: sendReponse = activity as sendReponse
//        send.completed(false)
        this.dismiss()
    }

/*    */
    /**
     * Interface dùng để trả giá trị
     *//*
    public interface sendReponse {
        fun completed(input: Boolean)
    }*/
}