package com.example.p3l_gofit

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_gofit.api.AuthApi
import com.example.p3l_gofit.databinding.FragmentHomeInstructorBinding
import com.example.p3l_gofit.databinding.FragmentLoginBinding
import com.example.p3l_gofit.models.Auth
import com.example.p3l_gofit.views.home.FragmentHome
import com.example.p3l_gofit.views.home.FragmentHomeInstructor
import com.example.p3l_gofit.views.home.FragmentHomeMember
import com.example.p3l_gofit.views.home.FragmentHomeMo
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Exception
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_login, container, false)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        queue = Volley.newRequestQueue(activity)

        var role = sharedPreferences.getString("role",null)
        var status = sharedPreferences.getString("status",null)

        if(role == "MO" && status == "logged") {
            transitionFragment(FragmentHomeMo())

        }
        if(role == "member" && status == "logged"){
            transitionFragment(FragmentHomeMember())

        }
        if(role == "instructor" && status == "logged"){
            transitionFragment(FragmentHomeInstructor())
        }
        binding.btnLogin.setOnClickListener {
            loginUser()
        }

        binding.textforgot.setOnClickListener {
            val Intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(Intent)
        }
    }
    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.layout_fragment, fragment)
            .addToBackStack(null).commit()
        transition.hide(LoginFragment())
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
                    MotionToast.darkColorToast(context as Activity,"Notification Login!",
                        "Succesfully Login!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(activity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    sharedPreferences.edit()
                        .putInt("id",userobj.getInt("ID_PEGAWAI"))
                        .putString("role","MO")
                        .putString("token",token)
                        .putString("status","logged")
                        .putString("name",userobj.getString("NAMA_PEGAWAI"))
                        .putString("booking",null)
                        .apply()
                    startActivity(intent)
                }
                else if(userobj.has("ID_MEMBER")) {
                    val token = resJO.getString("access_token")
                    MotionToast.darkColorToast(context as Activity,"Notification Login!",
                        "Succesfully Login!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(activity, HomeActivity::class.java)
                    sharedPreferences.edit()
                        .putString("id",userobj.getString("ID_MEMBER"))
                        .putString("role","member")
                        .putString("status","logged")
                        .putString("token",token)
                        .putString("name",userobj.getString("NAMA_MEMBER"))
                        .putString("booking",null)
                        .apply()
                    startActivity(intent)
                }
                else if(userobj.has("ID_INSTRUKTUR")) {
                    val token = resJO.getString("access_token")
                    MotionToast.darkColorToast(context as Activity,"Notification Login!",
                        "Succesfully Login!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(activity, HomeActivity::class.java)
                    sharedPreferences.edit()
                        .putInt("id",userobj.getInt("ID_INSTRUKTUR"))
                        .putString("role","instructor")
                        .putString("status","logged")
                        .putString("token",token)
                        .putString("name",userobj.getString("NAMA_INSTRUKTUR"))
                        .putString("booking",null)
                        .apply()
                    startActivity(intent)
                }
                else {
                    MotionToast.darkColorToast(context as Activity,"Notification Login!",
                        "Failed Login",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    MotionToast.darkColorToast(context as Activity,"Notification Login!",
                        errors.getString("message"),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: Exception) {
                    Toast.makeText(activity, e.message,
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