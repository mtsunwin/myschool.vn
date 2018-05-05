package com.iuh.tranthang.myshool.ViewApdater

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.iuh.tranthang.myshool.R


/**
 * Created by ThinkPad on 5/5/2018.
 */
class DialogAdapter : DialogFragment() {

    private lateinit var btnApply: Button
    private lateinit var txtContent: TextView
    //Được dùng khi khởi tạo dialog mục đích nhận giá trị
    fun newInstance(data: String): DialogAdapter {
        var dialog: DialogAdapter = DialogAdapter()
        val arg: Bundle = Bundle()
        arg.putString("data", data)
        dialog.arguments = arg
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.layout_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // lấy giá trị tự bundle
        val data = arguments.getString("data", "")
        txtContent = view.findViewById(R.id.txtDialog_content)
        btnApply = view.findViewById(R.id.btnDialog_apply)
        txtContent.text = data
        btnApply.setOnClickListener { view ->
            var send: sendReponse = activity as sendReponse
            send.completed(true)
            this.dismiss()
        }


    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        var send: sendReponse = activity as sendReponse
        send.completed(false)
        this.dismiss()
    }

    public interface sendReponse {
        fun completed(input: Boolean)
    }
}
