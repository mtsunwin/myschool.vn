package com.iuh.tranthang.myshool.ViewApdater

/**
 * Created by ThinkPad on 5/6/2018.
 */
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.iuh.tranthang.myshool.R

/**
 * Created by ThinkPad on 5/5/2018.
 */
class AdapterDialogLoading : DialogFragment() {
    public val key_Data = "data"
    public val key_Data_Ic = "dataIc"
    private lateinit var txtContent: TextView

    //Được dùng khi khởi tạo dialog mục đích nhận giá trị
    fun newInstance(data: String, id: Int): AdapterDialogLoading {
        var dialogAsk: AdapterDialogLoading = AdapterDialogLoading()
        val arg: Bundle = Bundle()
        arg.putString(key_Data, data)
        arg.putInt(key_Data_Ic, id)
        dialogAsk.arguments = arg
        return dialogAsk
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.layout_dialog_loading, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDialog().getWindow().requestFeature(STYLE_NO_TITLE);// Tắt title của dialog
        // lấy giá trị tự bundle
        val data = arguments.getString(key_Data, "")
        txtContent = view.findViewById(R.id.txtDialogLoading_title)
        txtContent.text = data
        var progressBar: ProgressBar = view.findViewById(R.id.progressBarNotifi)
        progressBar.setIndeterminate(true)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        Log.e("tmt", "dis")
    }

    /**
     * Interface dùng để trả giá trị
     */
    public interface sendReponse {
        fun completed(input: Boolean)
    }
}
