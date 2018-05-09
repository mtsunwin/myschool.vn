package com.iuh.tranthang.myshool

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.Firebase.NotificationUtils
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.ViewApdater.*
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.Parameter_Notification
import com.iuh.tranthang.myshool.model.mNotification
import com.iuh.tranthang.myshool.model.mNotificationUser
import kotlinx.android.synthetic.main.activity_create_notification.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class CreateNotificationActivity : AppCompatActivity(), AdapterDialogAsk.sendReponse, dbConnect.sendReponse,
        SwipeRefreshLayout.OnRefreshListener {

    val CONNECTON_TIMEOUT_MILLISECONDS = 60000
    private lateinit var notification: NotificationUtils
    private lateinit var awesomeValidation: AwesomeValidation
    private lateinit var dbFireStore: FirebaseFirestore
    private lateinit var mAuth: FirebaseUser
    private lateinit var db: dbConnect
    private lateinit var recycleView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_notification)

        notification = NotificationUtils(applicationContext)
        db = dbConnect(this)
        dbFireStore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance().currentUser!!
        db.getListNotificationTemplate()

        // Validation
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation!!.addValidation(this, R.id.txt_titleNotification, "([a-zA-Z' ]+){6,}", R.string.validation_titleNotification)
        awesomeValidation!!.addValidation(this, R.id.txt_contentNotification, "([a-zA-Z' ]+){12,}", R.string.validation_contentNotification)

        // END - Validation
        swipe_notification.setOnRefreshListener { onRefresh() }
        spinner_list.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.select_notification_to_send))
        val actionBar = supportActionBar
        actionBar!!.hide()
        // NÚT Tạo tin nhắn mẫu
        btn_createTemplate.setOnClickListener { view ->
            if (awesomeValidation!!.validate()) {
//                val fm: FragmentManager? = supportFragmentManager
//                val userInfoDialogAsk: AdapterDialogAsk = AdapterDialogAsk().newInstance("Bạn Có muốn thêm" +
//                        txt_titleNotification.text + " vào tin nhắn mẫu?", R.drawable.ic_add_white_24dp)
//                userInfoDialogAsk.show(fm, null)
                var str: String = "Bạn Có muốn thêm<br> <b>" +
                        txt_titleNotification.text + "</b><br>vào tin nhắn mẫu?"
                showDialog(str, 0, "")
            }
        }
        // NÚT Gửi thông báo
        btn_sentNotification.setOnClickListener {
            val fm: FragmentManager? = supportFragmentManager
            val dialogLoading: AdapterDialogLoading = AdapterDialogLoading().newInstance(
                    "Đang xử lý bạn vui lòng chờ...", 0)
            dialogLoading.show(fm, null)

            if (awesomeValidation!!.validate()) {

                var strTitle = txt_titleNotification.text
                var strContent = txt_contentNotification.text
                var url = "http://ngansotre.com/thangtm/getNotification.php/random_user?title=" +
                        strTitle + "&content=" + strContent
                val listStringPermission = resources.getStringArray(R.array.select_notification_to_send)
                when (spinner_list.selectedItem) {
                    listStringPermission[0] -> {
                        dbFireStore.collection(Parameter.root_User)
                                .get()
                                .addOnCompleteListener({ task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result) {
                                            var idDevice = document.data[Parameter.comp_uidDevice] as String
                                            if (document.data[Parameter.comp_action].toString() == "true"
                                                    && idDevice.length > 0) {
                                                url += "&regid=" + idDevice
                                                pushNotification().execute(url)
                                            }
                                        }
                                    } else {
                                        Log.d("tmt", "Error getting documents: ", task.exception)
                                        // Lỗi trả về
                                    }
                                })
                    }
                    listStringPermission[1] -> {
                        dbFireStore.collection(Parameter.root_User)
                                .get()
                                .addOnCompleteListener({ task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result) {
                                            var idDevice = document.data[Parameter.comp_uidDevice] as String
                                            if (document.data[Parameter.comp_action].toString() == "true"
                                                    && idDevice.length > 0
                                                    && document.data[Parameter.comp_Permission] == 1) {
                                                url += "&regid=" + idDevice
                                                pushNotification().execute(url)
                                            }
                                        }
                                    } else {
                                        Log.d("tmt", "Error getting documents: ", task.exception)
                                        // Lỗi trả về
                                    }
                                })
                    }
                    listStringPermission[2] -> {
                        dbFireStore.collection(Parameter.root_User)
                                .get()
                                .addOnCompleteListener({ task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result) {
                                            var idDevice = document.data[Parameter.comp_uidDevice] as String
                                            if (document.data[Parameter.comp_action].toString() == "true"
                                                    && idDevice.length > 0
                                                    && document.data[Parameter.comp_Permission] == 2) {
                                                url += "&regid=" + idDevice
                                                pushNotification().execute(url)
                                            }
                                        }
                                    } else {
                                        Log.d("tmt", "Error getting documents: ", task.exception)
                                        // Lỗi trả về
                                    }
                                })
                    }
                    listStringPermission[3] -> {
                        dbFireStore.collection(Parameter.root_User)
                                .get()
                                .addOnCompleteListener({ task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result) {
                                            var idDevice = document.data[Parameter.comp_uidDevice] as String
                                            if (document.data[Parameter.comp_action].toString() == "true"
                                                    && idDevice.length > 0
                                                    && document.data[Parameter.comp_Permission] == 3) {
                                                url += "&regid=" + idDevice
                                                pushNotification().execute(url)
                                            }
                                        }
                                    } else {
                                        Log.d("tmt", "Error getting documents: ", task.exception)
                                        // Lỗi trả về
                                    }
                                })
                    }
                    listStringPermission[4] -> {
                        dbFireStore.collection(Parameter.root_User)
                                .get()
                                .addOnCompleteListener({ task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result) {
                                            var idDevice = document.data[Parameter.comp_uidDevice] as String
                                            if (document.data[Parameter.comp_action].toString() == "true"
                                                    && idDevice.length > 0
                                                    && document.data[Parameter.comp_Permission] == 4) {
                                                url += "&regid=" + idDevice
                                                pushNotification().execute(url)
                                            }
                                        }
                                    } else {
                                        Log.d("tmt", "Error getting documents: ", task.exception)
                                        // Lỗi trả về
                                    }
                                })
                    }
                }
            }
        }
    }

    /**
     * Nhận dữ liệu từ db trả về
     * Lấy danh sách Các tin nhắn mẫu
     */
    override fun getListNotification_Template(list: ArrayList<mNotification>) {
        recycleView = findViewById(R.id.recycleView_notification)
        recycleView.layoutManager = LinearLayoutManager(this)
        if (list.size > 0) {
            val simpleAdapter = RecycleViewNotificationAdapter(list, { partItem: mNotification ->
                partItemClicked(partItem)
            })
            var adap = AdapterDataNotification(applicationContext, list)
            recycleView.adapter = simpleAdapter
            adap.notifyDataSetChanged()

            swipe_notification.setRefreshing(false)
            val swipeHandler = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                    val adapter = recycleView.adapter
                    Log.e("tmt", "swwwwwwwww")
                    var mNo: mNotification = list.get(viewHolder!!.adapterPosition)
                    val str: String = "Bạn muốn xóa <b>" + mNo.title + "</b> không?"
                    showDialog(str, 1, mNo.id)
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(recycleView)
        }
        swipe_notification.setRefreshing(false)
    }

    /**
     * Swipe Refesh Data
     */
    override fun onRefresh() {
        Log.e("tmt", "refresh")
        db.getListNotificationTemplate()
    }

    /**
     * Hiển thị Dialog hỏi người dùng có muốn xóa hay không
     */
    private fun showDialog(str: String, ask: Int, idNotificationDelted: String) {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        var inflater: LayoutInflater = layoutInflater
        var view: View = inflater.inflate(R.layout.layout_dialog, null)
        var content: TextView = view.findViewById<View>(R.id.txtDialog_content) as TextView
        content.setText(Html.fromHtml(str))
        builder.setView(view)
        builder.setNegativeButton(R.string.dialogAsk_no, object : DialogInterface.OnClickListener { // cancel
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0!!.dismiss()
                val adapter = recycleView.adapter
                adapter.notifyDataSetChanged()
            }
        })
        builder.setPositiveButton(R.string.dialogAsk_yes, object : DialogInterface.OnClickListener { // apply
            override fun onClick(p0: DialogInterface?, p1: Int) {
                when (ask) {
                    0 -> { // Tạo tin nhắn mẫu
                        val listStringPermission = resources.getStringArray(R.array.select_notification_to_send)
                        var number: String = "0"
                        when (spinner_list.selectedItem) {
                            listStringPermission[1] -> {
                                number = "1"
                            }
                            listStringPermission[2] -> {
                                number = "2"
                            }
                            listStringPermission[3] -> {
                                number = "3"
                            }
                            listStringPermission[4] -> {
                                number = "4"
                            }
                        }
                        var cal = Calendar.getInstance()
                        var date = cal.time
                        var idDocument = dbFireStore.collection(Parameter_Notification.collection).document()
                        var mNo = mNotification(idDocument.id, txt_titleNotification.text.toString(),
                                txt_contentNotification.text.toString(), number)
                        mNo.count = "0"
                        mNo.listView = ArrayList<mNotificationUser>()
                        mNo.dateTime = SimpleDateFormat("dd/MM/yyyy hh:mm:ss aaa").format(date)
                        idDocument.set(mNo).addOnCompleteListener { task ->
                            clearText();
                            db.getListNotificationTemplate()
                        }
                    }
                    1 -> {
                        db.deleteItemNotificationTemplate(idNotificationDelted)
                    }
                }
            }
            
        })
        var dialog: Dialog = builder.create()
        dialog.show()
    }

    /**
     * Clear các file text trên giao diện
     */
    private fun clearText() {
        txt_contentNotification.setText("")
        txt_titleNotification.setText("")
        spinner_list.setSelection(0)
    }

    private fun runn(): Boolean {
        val message = "Test Notification"
        val resultIntent = Intent(applicationContext, AdminActivity::class.java)
        resultIntent.putExtra("message", message)
        notification!!.showNotificationMessage("Thang dep trai", message, "", resultIntent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_back, menu)
        return super.onCreateOptionsMenu(menu)
    }

    inner class pushNotification : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg urls: String?): String {
            var urlConnection: HttpURLConnection? = null
            try {
                val url = URL(urls[0])
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = CONNECTON_TIMEOUT_MILLISECONDS
                urlConnection.readTimeout = CONNECTON_TIMEOUT_MILLISECONDS
                var inString = streamToString(urlConnection.inputStream)
                publishProgress(inString)
            } catch (ex: Exception) {

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }
            }
            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            Log.e("tmt", values[0])
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }
    }

    fun streamToString(inputStream: InputStream): String {
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line: String
        var result = ""
        try {
            do {
                line = bufferReader.readLine()
                if (line != null) {
                    result += line
                }
            } while (line != null)
            inputStream.close()
        } catch (ex: Exception) {

        }
        return result
    }

    /**
     * Nhận dữ liệu trả vè từ Dialog Adapter
     */
    override fun completed(input: Boolean) {

    }

    /**
     * Nhận dữ liệu trả vè từ DB
     * Xóa User
     * True -> xóa thành công
     * False -> xóa thất bại
     */
    override fun deleteItemNotification_Template(status: Boolean) {
        if (status) {
            swipe_notification.setRefreshing(true)
            db.getListNotificationTemplate()
        } else {
            Log.e("tmt", "xóa không thành công")
        }
    }

    private fun partItemClicked(partItem: mNotification) {
        txt_titleNotification.setText(partItem.title)
        txt_contentNotification.setText(partItem.content)
        val listStringPermission = resources.getStringArray(R.array.select_notification_to_send)
        spinner_list.setSelection(partItem.group.toInt())
    }
}
