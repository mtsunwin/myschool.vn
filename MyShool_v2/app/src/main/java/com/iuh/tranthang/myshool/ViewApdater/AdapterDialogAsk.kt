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
import kotlinx.android.synthetic.main.layout_dialog.*


/**
 * Created by ThinkPad on 5/5/2018.
 */
class AdapterDialogAsk : DialogFragment() {
    public val key_Data = "data"
    public val key_Data_Ic = "dataIc"
    private lateinit var btnApply: Button
    private lateinit var btnCancel: Button
    private lateinit var txtContent: TextView

    //Được dùng khi khởi tạo dialog mục đích nhận giá trị
    fun newInstance(data: String, id: Int): AdapterDialogAsk {
        var dialogAsk: AdapterDialogAsk = AdapterDialogAsk()
        val arg: Bundle = Bundle()
        arg.putString(key_Data, data)
        arg.putString(key_Data_Ic, data)
        dialogAsk.arguments = arg
        return dialogAsk
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.layout_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDialog().getWindow().requestFeature(STYLE_NO_TITLE);// Tắt title của dialog
        // lấy giá trị tự bundle
        val data = arguments.getString(key_Data, "")
        val dataIc: Int = arguments.getInt(key_Data_Ic, 0)

        txtContent = view.findViewById(R.id.txtDialog_content)
        txtContent.text = data
        ic_log.setImageResource(dataIc)


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

    /**
     * Interface dùng để trả giá trị
     */
    public interface sendReponse {
        fun completed(input: Boolean)
    }
}
