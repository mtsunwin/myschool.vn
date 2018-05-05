package com.iuh.tranthang.myshool

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.widget.ArrayAdapter
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.Firebase.NotificationUtils
import com.iuh.tranthang.myshool.ViewApdater.DialogAdapter
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
import kotlin.collections.ArrayList

class CreateNotificationActivity : AppCompatActivity(), DialogAdapter.sendReponse {
    override fun completed(input: Boolean) {
        if (input) {
            val listStringPermission = resources.getStringArray(R.array.select_notification_to_send)
            var number: Int = 0
            when (spinner_list.selectedItem) {
                listStringPermission[1] -> {
                    number = 1
                }
                listStringPermission[2] -> {
                    number = 2
                }
                listStringPermission[3] -> {
                    number = 3
                }
                listStringPermission[4] -> {
                    number = 4
                }
            }
            var cal = Calendar.getInstance()
            var date = cal.time

            var idDocument = dbFireStore.collection(Parameter_Notification.collection).document()

            var mNo = mNotification(idDocument.id, txt_titleNotification.text.toString(), txt_contentNotification.text.toString()
                    , number)
            mNo.count = 0
            mNo.listView = ArrayList<mNotificationUser>()
            mNo.dateTime = SimpleDateFormat("dd/MM/yyyy hh:mm:ss aaa").format(date)
            idDocument.set(mNo).addOnCompleteListener { task ->

            }
        } else {

        }
    }


    val CONNECTON_TIMEOUT_MILLISECONDS = 60000
    private lateinit var notification: NotificationUtils
    private lateinit var awesomeValidation: AwesomeValidation
    private lateinit var dbFireStore: FirebaseFirestore
    private lateinit var mAuth: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_notification)
        notification = NotificationUtils(applicationContext)

        dbFireStore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance().currentUser!!

        // Validation
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation!!.addValidation(this, R.id.txt_titleNotification, "([a-zA-Z' ]+){6,}", R.string.validation_titleNotification)
        awesomeValidation!!.addValidation(this, R.id.txt_contentNotification, "([a-zA-Z' ]+){12,}", R.string.validation_contentNotification)
        // END - Validation

        spinner_list.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.select_notification_to_send))
        val actionBar = supportActionBar
        actionBar!!.hide()

        // NÚT Tạo tin nhắn mẫu
        btn_createTemplate.setOnClickListener { view ->
            if (awesomeValidation!!.validate()) {
                val fm: FragmentManager? = supportFragmentManager
                val userInfoDialog: DialogAdapter = DialogAdapter().newInstance("Bạn Có muốn thêm" +
                        txt_titleNotification.text + " vào tin nhắn mẫu?")
                userInfoDialog.show(fm, null)
            }
        }

        // NÚT Gửi thông báo
        btn_sentNotification.setOnClickListener {
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

}
