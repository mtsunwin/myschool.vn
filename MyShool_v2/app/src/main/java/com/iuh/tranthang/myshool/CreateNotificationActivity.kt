package com.iuh.tranthang.myshool

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_notification)
        notification = NotificationUtils(applicationContext)

//        btnCreatedNotification.setOnClickListener { view ->
//            Log.e("tmt", "Thang dep trai da click vao day")
//            runn()
//        }

        val actionBar = supportActionBar
        actionBar!!.hide()
        btn_createTemplate.setOnClickListener { view ->
            val url = "http://ngansotre.com/thangtm/getNotification.php/random_user?thang=deptrai"
            pushNotification().execute(url)
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
//            var json = JSONObject(values[0])
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
