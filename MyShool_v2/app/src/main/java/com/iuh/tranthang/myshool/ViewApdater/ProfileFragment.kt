package com.iuh.tranthang.myshool.ViewApdater


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.ProfileActivity
import com.iuh.tranthang.myshool.R
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.mUser
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private var txt_address: String = ""
    private var txt_phone: String = ""
    private var txt_email: String = ""
    private var txt_birthday: String = ""
    private var txt_luong:Float?=null
    private var txt_basesalary:String?=""
    private var luongCB:String?=""
    private var txt_luongThang:String?=null

    interface OnSelectedListener {
        fun onSelected(dMUser: mUser)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var mView: View = inflater!!.inflate(R.layout.fragment_profile, container, false)
        // Inflate the layout for this fragment
        return mView
    }

    private fun updateUI() {

        Log.e("tmt", txt_address.length.toString() + " - " + txt_phone.length.toString()
                + " - " + txt_email.length.toString() + " - " + txt_birthday.length.toString())
        frm_txtAddress.setText(if (txt_address.length > 0) txt_address else resources.getString(R.string.updateInfor))
        frm_txtNumPhone.setText(if (txt_phone.length > 0) txt_phone else resources.getString(R.string.updateInfor))
        frm_txtEmail.setText(if (txt_email.length > 0) txt_email else resources.getString(R.string.updateInfor))
        frm_txtBirthday.setText(if (txt_birthday.length > 0) txt_birthday else resources.getString(R.string.updateInfor))
        frm_txtLuong.setText(if(txt_luong!!>0) txt_luong.toString() else resources.getString(R.string.updateInfor))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        txt_address = arguments.getString(ProfileActivity().frm_address)
        txt_phone = arguments.getString(ProfileActivity().frm_phone)
        txt_email = arguments.getString(ProfileActivity().frm_email)
        txt_birthday = arguments.getString(ProfileActivity().frm_birthday)
        txt_luong=arguments.getString(ProfileActivity().frm_luong).toFloat()

        updateUI()
    }
}// Required empty public constructor
