package com.iuh.tranthang.myshool

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.model.Parameter
import kotlinx.android.synthetic.main.activity_duyen_don_apply.*

class DuyenDonApplyActivity : AppCompatActivity(), dbConnect.donxin {


    private lateinit var id: String
    private lateinit var name: String
    private lateinit var content: String
    private lateinit var date: String
    private lateinit var db: dbConnect

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_duyen_don_apply)
        db = dbConnect(this)

        id = intent.getStringExtra(Parameter.takeLeave_id)
        name = intent.getStringExtra(Parameter.takeLeave_title)
        content = intent.getStringExtra(Parameter.takeLeave_content)
        date = intent.getStringExtra(Parameter.takeLeave_date)

        val actionBar = supportActionBar
        actionBar!!.hide()

        var str = ""
        str += "<b>" + name + "<b><br>"
        str += content + "<br>"
        str += "Thời gian: " + date
        duyetdon_content.text = Html.fromHtml(str)
        btn_chapnhan.setOnClickListener { view ->
            db.updateDonXin(id)
        }
        btn_trove.setOnClickListener { view ->
            finish()
        }
    }

    override fun getdonxin(status: Boolean) {
        if (status) {
            finish()
        } else {
            finish()
        }
    }
}
