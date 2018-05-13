package com.iuh.tranthang.myshool

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.model.Parameter_Take_Leaves
import com.iuh.tranthang.myshool.model.mTakeLeave
import kotlinx.android.synthetic.main.activity_take_leave.*
import java.text.SimpleDateFormat
import java.util.*

class takeLeaveActivity : AppCompatActivity() {

    private lateinit var dbFireStore: FirebaseFirestore
    private lateinit var mAuth: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_leave)

        dbFireStore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance().currentUser!!

        val actionBar = supportActionBar
        actionBar!!.hide()

        var calendar = Calendar.getInstance()
        val dateFormat = "dd/MM/yyyy"
        val DateFragmentStart = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val sdf = SimpleDateFormat(dateFormat, Locale.US)
            txtTakeLeave_timeStart.setText(sdf.format(calendar.time))
        }

        val DateFragmentEnd = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val sdf = SimpleDateFormat(dateFormat, Locale.US)
            txtTakeLeave_timeEnd.setText(sdf.format(calendar.time))
        }

        txtTakeLeave_timeStart.setOnClickListener { view ->
            DatePickerDialog(this, DateFragmentStart,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        txtTakeLeave_timeEnd.setOnClickListener { view ->
            DatePickerDialog(this, DateFragmentEnd,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnTakeLeave_gui.setOnClickListener { view ->
            when (checkText()) {
                1 -> {
                    txtTakeLeave_content.isFocusable = true
                    txtTakeLeave_content.error = resources.getString(R.string.validation_number)
                }
                2 -> {
                    txtTakeLeave_timeStart.isFocusable = true
                    txtTakeLeave_timeStart.error = "Thời gian nhận đơn xử lý trước 7 ngày"
                }
                3 -> {
                    txtTakeLeave_timeEnd.isFocusable = true
                    txtTakeLeave_timeEnd.error = "Thời gian nghỉ không hợp lệ"
                }
                4 -> {
                    Toast.makeText(this, "Ngày tháng không hợp lệ", Toast.LENGTH_LONG).show()
                }
                0 -> {
                    updateToDB();
                }

            }
        }
    }

    /**
     * Thực hiện Update lên Db
     */
    private fun updateToDB() {
        var idDocument = dbFireStore.collection(Parameter_Take_Leaves.collection).document()
        var mTakeLeave = mTakeLeave(idDocument.id, txtTakeLeave_content.text.toString(),
                txtTakeLeave_timeStart.text.toString(),
                txtTakeLeave_timeEnd.text.toString(),
                "1", mAuth.uid)
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(android.R.layout.select_dialog_item, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle("Đơn xin nghỉ phép")
        var temp = "<b>Lý do:</b><br>" + txtTakeLeave_content.text.toString() +
                "<br><br><b>Từ ngày: </b>" + txtTakeLeave_timeStart.text.toString() +
                "<br><b>Đến ngày: </b>" + txtTakeLeave_timeEnd.text.toString()
        dialogBuilder.setMessage(Html.fromHtml(temp))
        dialogBuilder.setPositiveButton("Chấp nhận", DialogInterface.OnClickListener { dialog, whichButton ->
            idDocument.set(mTakeLeave).addOnCompleteListener { task ->
                val intent = Intent(this, ATeacherActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        dialogBuilder.setNegativeButton("Hủy bỏ", DialogInterface.OnClickListener { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }

    /**
     * Kiểm tra giá trị
     */
    private fun checkText(): Int {
        var timeStart = txtTakeLeave_timeStart.text
        var timeEnd = txtTakeLeave_timeEnd.text
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val today = Date(System.currentTimeMillis())
        if (timeStart.length > 6 && timeEnd.length > 6) {
            val dateStart = formatter.parse(timeStart.toString())
            val dateEnd = formatter.parse(timeEnd.toString())

            if (dateStart.date.toInt() < today.date.toInt() + 6 && dateStart.month.toInt() <= today.month.toInt()
                    && dateStart.year.toInt() <= today.year.toInt()) {
                return 2
            }

            if (dateEnd.date.toInt() < dateStart.date.toInt() && dateEnd.month.toInt() <= dateStart.month.toInt()
                    && dateEnd.year.toInt() <= dateStart.year.toInt()) {
                return 3
            }
        } else {
            return 4
        }
        if (txtTakeLeave_content.length() < 6) {
            return 1
        }
        return 0
    }
}
