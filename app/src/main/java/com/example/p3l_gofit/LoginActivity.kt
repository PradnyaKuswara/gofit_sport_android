package com.example.p3l_gofit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_gofit.api.AuthApi
import com.example.p3l_gofit.databinding.ActivityLoginBinding
import com.example.p3l_gofit.models.Auth
import com.example.p3l_gofit.models.Member
import com.example.p3l_gofit.models.Pegawai
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.Exception
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityLoginBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)
        queue = Volley.newRequestQueue(this)

        binding.btnLogin.setOnClickListener(View.OnClickListener {
            loginUser()
        })

        binding.btnForgot.setOnClickListener {
            val Intent = Intent(this@LoginActivity, ChangePasswordActivity::class.java)
            startActivity(Intent)
        }
    }

    private fun loginUser(){
        val auth = Auth(
            binding.inputLayoutEmail.getEditText()?.getText().toString(),
            binding.inputLayoutPassword.getEditText()?.getText().toString())

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, AuthApi.LOGIN, Response.Listener { response ->
                val gson = Gson()
//                var user_pegawai = gson.fromJson(response, Pegawai::class.java)
//                var user_member = gson.fromJson(response, Member::class.java)

                var resJO = JSONObject(response.toString())
                val userobj = resJO.getJSONObject("user")

                if(userobj.has("ID_PEGAWAI")) {
                    val token = resJO.getString("access_token")
                    MotionToast.darkColorToast(this,"Notification Login!",
                        "Succesfully Login!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    sharedPreferences.edit()
                        .putInt("id",userobj.getInt("ID_PEGAWAI"))
                        .putString("role","MO")
                        .putString("token",token)
                        .putString("status","logged")
                        .putString("name",userobj.getString("NAMA_PEGAWAI"))
                        .apply()
                    startActivity(intent)
                }
                else if(userobj.has("ID_MEMBER")) {
                    val token = resJO.getString("access_token")
                    MotionToast.darkColorToast(this,"Notification Login!",
                        "Succesfully Login!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    sharedPreferences.edit()
                        .putString("id",userobj.getString("ID_MEMBER"))
                        .putString("role","member")
                        .putString("status","logged")
                        .putString("token",token)
                        .putString("name",userobj.getString("NAMA_MEMBER"))
                        .apply()
                    startActivity(intent)
                }
                else if(userobj.has("ID_INSTRUKTUR")) {
                    val token = resJO.getString("access_token")
                    MotionToast.darkColorToast(this,"Notification Login!",
                        "Succesfully Login!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    sharedPreferences.edit()
                        .putInt("id",userobj.getInt("ID_INSTRUKTUR"))
                        .putString("role","instructor")
                        .putString("status","logged")
                        .putString("name",userobj.getString("NAMA_INSTRUKTUR"))
                        .putString("token",token)
                        .apply()
                    startActivity(intent)
                }
                else {
                    MotionToast.darkColorToast(this,"Notification Login!",
                        "Failed Login",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    MotionToast.darkColorToast(this,"Notification Login!",
                        errors.getString("message"),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, e.message,
                        Toast.LENGTH_LONG).show();
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(auth)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}