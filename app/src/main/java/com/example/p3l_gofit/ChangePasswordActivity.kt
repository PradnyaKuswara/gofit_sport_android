package com.example.p3l_gofit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_gofit.api.AuthApi
import com.example.p3l_gofit.databinding.ActivityChangePasswordBinding
import com.example.p3l_gofit.databinding.ActivityLoginBinding
import com.example.p3l_gofit.models.Auth
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Exception
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityChangePasswordBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)
        queue = Volley.newRequestQueue(this)

        binding.btnSubmit.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword(){
        val auth = Auth(
            binding.inputLayoutEmail.getEditText()?.getText().toString(),
            binding.inputLayoutPassword.getEditText()?.getText().toString())

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, AuthApi.FORGOT, Response.Listener { response ->
                val gson = Gson()
//                var user_pegawai = gson.fromJson(response, Pegawai::class.java)
//                var user_member = gson.fromJson(response, Member::class.java)

                var user= gson.fromJson(response, Auth::class.java)

                var resJO = JSONObject(response.toString())
                val userobj = resJO.getJSONObject("user")

                if(user!=null) {
                    MotionToast.darkColorToast(this,"Notification Authentication!",
                        "Succesfully Change Password!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@ChangePasswordActivity, HomeActivity::class.java)

                    startActivity(intent)
                }
                else {
                    MotionToast.darkColorToast(this,"Notification Authentication!",
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
                    MotionToast.darkColorToast(this,"Notification Authentication!",
                        errors.getString("message"),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: Exception) {
                    MotionToast.darkColorToast(this,"Notification Authentication!",
                        e.message.toString(),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
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