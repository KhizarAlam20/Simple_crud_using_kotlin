package com.example.og

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.security.SecureRandom
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.X509Certificate

class MainActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var id: EditText
    lateinit var name: EditText
    lateinit var password: EditText
    lateinit var login: Button
    lateinit var update: Button
    lateinit var delete: Button
    lateinit var display: Button
    lateinit var perf: TextView

    lateinit var listView: ListView
    var arrayListEmployee: ArrayList<employees> = ArrayList()
    lateinit var listAdapter: ListAdapter

    var url: String = "https://192.168.10.3/api/"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //add these to skip/add all certs
        try {
            val trustAllCerts: Array<TrustManager> = arrayOf(
                object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<out java.security.cert.X509Certificate>?,
                        authType: String?
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<out java.security.cert.X509Certificate>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }

        } catch (e: Exception) {
            e.printStackTrace()
        }


        email = findViewById(R.id.email)
        id = findViewById(R.id.id)
        name = findViewById(R.id.name)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        update = findViewById(R.id.update)
        delete = findViewById(R.id.delete)
        display = findViewById(R.id.display)
        perf = findViewById(R.id.perf)

        listView = findViewById(R.id.listView)
        arrayListEmployee = ArrayList()
        listAdapter = ListAdapter(this, arrayListEmployee)
        listView.adapter = listAdapter




        login.setOnClickListener {
            insert()
        }

        display.setOnClickListener {
            display()
        }

        update.setOnClickListener {
            update()
        }

        delete.setOnClickListener {
            delete()
        }
    }

    //CRUD
    fun display() {

        var URLL: String = url + "display.php"

        val request = StringRequest(Request.Method.POST, URLL,
            Response.Listener<String> { response ->
                Log.d("Response", response)
                Log.d("Before success", response)
                arrayListEmployee.clear()

                Toast.makeText(applicationContext, "hi", Toast.LENGTH_SHORT).show()
                try {
                    var jsonObject = JSONObject(response)
                    perf.text = "before success"
                    var success = jsonObject.getString("success")
                    var jsonArray = jsonObject.getJSONArray("employees")
                    println(success + " <- success from .php")
                    Log.d("hello -------------------------", response)
                    Log.d("Before success", success)

                    if (success == "1") {
                        Log.d("hello -------------------------", response)

                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)

                            val idV = obj.getString("id")
                            val name = obj.getString("name")
                            val email = obj.getString("email")
                            val password = obj.getString("password")

                            perf.text = name

                            if (idV.equals(id.text.toString())) {
                                val employee = employees(idV, name, email, password)
                                arrayListEmployee.add(employee)
                                listAdapter.notifyDataSetChanged()
                            }else{
                                Toast.makeText(this@MainActivity, "No Matching id found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Handle the error condition
                        Log.d("Error", "Failed to retrieve data")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            })

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }

    fun insert() {
        val nameValue: String = name.text.toString().trim()
        val emailValue: String = email.text.toString().trim()
        val passwordValue: String = password.text.toString().trim()
        var URL: String = url + "conn.php"

        if (passwordValue.isEmpty()) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
            return
        }

        println("Hello After")

        val stringRequest = object : StringRequest(Request.Method.POST, URL,
            Response.Listener { response ->
                if (response.equals("inserted", ignoreCase = true)) {
                    Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.message + " | " + URL, Toast.LENGTH_SHORT).show()

            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["name"] = nameValue
                params["email"] = emailValue
                params["password"] = passwordValue
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    fun update() {
        val idValue: String = id.text.toString().trim()
        val nameValue: String = name.text.toString().trim()
        val emailValue: String = email.text.toString().trim()
        val passwordValue: String = password.text.toString().trim()

        if (idValue.isEmpty()) {
            Toast.makeText(this, "Enter ID", Toast.LENGTH_SHORT).show()
            return
        }

        var URL: String = url + "update.php"

        val stringRequest = object : StringRequest(Request.Method.POST, URL,
            Response.Listener { response ->
                if (response.equals("updated", ignoreCase = true)) {
                    Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.message + " | " + URL, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = idValue
                params["name"] = nameValue
                params["email"] = emailValue
                params["password"] = passwordValue
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    @SuppressLint("SuspiciousIndentation")
    fun delete() {
        val idValue: String = id.text.toString().trim()

        if (idValue.isEmpty()) {
            Toast.makeText(this, "Enter ID", Toast.LENGTH_SHORT).show()
            return
        }

        var URL: String = url + "delete.php"

        val stringRequest = object : StringRequest(Request.Method.POST, URL,
            Response.Listener { response ->
                if (response.equals("deleted", ignoreCase = true)) {
                    Toast.makeText(this, "Data Deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.message + " | " + URL, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = idValue
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }


}
