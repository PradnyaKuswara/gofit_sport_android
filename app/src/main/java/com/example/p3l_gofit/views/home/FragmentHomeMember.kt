package com.example.p3l_gofit.views.home

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
import com.example.p3l_gofit.HomeActivity
import com.example.p3l_gofit.LoginActivity
import com.example.p3l_gofit.R
import com.example.p3l_gofit.api.AuthApi
import com.example.p3l_gofit.databinding.FragmentHomeMemberBinding
import com.example.p3l_gofit.models.Auth
import com.example.p3l_gofit.views.booking_class.BookingClassActivity
import com.example.p3l_gofit.views.booking_class.HistoryBookingActivity
import com.example.p3l_gofit.views.booking_gym.HistoryBookingGymActivity
import com.example.p3l_gofit.views.permission.PermissionActivity
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Exception
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws


class FragmentHomeMember : Fragment() {
    private var _binding : FragmentHomeMemberBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home_member, container, false)
        _binding = FragmentHomeMemberBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        queue = Volley.newRequestQueue(activity)

        binding.cardViewMember.setOnClickListener {
            val intent = Intent(activity, HistoryBookingActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewMember2.setOnClickListener {
            logoutUser()
        }

        binding.cardViewMember3.setOnClickListener {
            val intent = Intent(activity, HistoryBookingGymActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logoutUser(){
        val auth = Auth(
            "",
            "")

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, AuthApi.LOGOUT, Response.Listener { response ->
                val gson = Gson()
                var user_logout = gson.fromJson(response, Auth::class.java)

//                var resJO = JSONObject(response.toString())
//                val userobj = resJO.getJSONObject("user")

                if(user_logout != null) {
//                    val token = resJO.getString("access_token")
                    MotionToast.darkColorToast(context as Activity,"Notification Logout!",
                        "Succesfully Logout!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(activity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    sharedPreferences.edit()
                        .putInt("id",-1)
                        .putString("id", null)
                        .putString("role",null)
                        .putString("token",null)
                        .putString("status",null)
                        .putString("name",null)
                        .apply()
//                    sharedPreferences.edit().clear().commit();
                    startActivity(intent)
                }
                else {
                    MotionToast.darkColorToast(context as Activity,"Notification Logout!",
                        "Failed Logout",
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
                    MotionToast.darkColorToast(context as Activity,"Notification Logout!",
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
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
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