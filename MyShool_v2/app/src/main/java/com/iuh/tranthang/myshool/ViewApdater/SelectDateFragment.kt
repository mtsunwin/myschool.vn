package com.iuh.tranthang.myshool.ViewApdater

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.DatePicker
import java.io.Console
import java.util.*

/**
 * Created by ThinkPad on 4/17/2018.
 */
class SelectDateFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePicker?, yy: Int, mm: Int, dd: Int) {

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var calendar: Calendar = Calendar.getInstance()
        var yy: Int = calendar.get(Calendar.YEAR)
        var mm: Int = calendar.get(Calendar.MONTH)
        var dd: Int = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, this, yy, mm, dd)
    }
}