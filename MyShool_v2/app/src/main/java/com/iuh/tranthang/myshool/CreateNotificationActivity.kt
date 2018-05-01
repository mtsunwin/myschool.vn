package com.iuh.tranthang.myshool

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.iuh.tranthang.myshool.Firebase.NotificationUtils
import kotlinx.android.synthetic.main.activity_create_notification.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CreateNotificationActivity : AppCompatActivity() {


    val CONNECTON_TIMEOUT_MILLISECONDS = 60000
    var notification: NotificationUtils? = null
    private var awesomeValidation: AwesomeValidation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_notification)

        notification = NotificationUtils(applicationContext)

        // Validation
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation!!.addValidation(this, R.id.txt_titleNotification, "([a-zA-Z' ]+){6,}", R.string.validation_titleNotification)
        awesomeValidation!!.addValidation(this, R.id.txt_contentNotification, "([a-zA-Z' ]+){12,}", R.string.validation_contentNotification)
        // END - Validation

        val actionBar = supportActionBar
        actionBar!!.hide()
        btn_createTemplate.setOnClickListener { view ->

        }
        btn_sentNotification.setOnClickListener {
            if (awesomeValidation!!.validate()) {
                var strTitle = txt_titleNotification.text
                var strContent = txt_contentNotification.text
                val url = "http://ngansotre.com/thangtm/getNotification.php/random_user?title=" +
                        strTitle + "&content=" + strContent
                pushNotification().execute(url)
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
            var sUrl = urls[0] + "&regid=dNxXUiIzzKs:APA91bHa39w7E7oQhIpIjLebCtnU3U7Y7gLCgcJwWUe-srqFgpRsnlfxHtCQxiLHtG7wr5kZH7FizYl03QYgDWg16Oj-HBK8XS_d6dLn2FUYgv5_TRl_j5FR64DYzMl9oUxXi4S6Z3as";
            var urlConnection: HttpURLConnection? = null
            try {
                val url = URL(sUrl)
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
