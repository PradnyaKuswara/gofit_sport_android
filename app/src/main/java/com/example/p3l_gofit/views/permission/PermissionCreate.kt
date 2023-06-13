package com.example.p3l_gofit.views.permission

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_gofit.HomeActivity
import com.example.p3l_gofit.R
import com.example.p3l_gofit.api.AuthApi
import com.example.p3l_gofit.api.InstructorPermissionApi
import com.example.p3l_gofit.databinding.ActivityPermissionCreateBinding
import com.example.p3l_gofit.models.Auth
import com.example.p3l_gofit.models.InstructorPermission
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class PermissionCreate : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var queue: RequestQueue? = null
    private lateinit var binding: ActivityPermissionCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionCreateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt("id",-1)

        queue = Volley.newRequestQueue(this)

        setDropdownSchedule(id)

        binding.btnStore.setOnClickListener {
            storePermission(id)
        }

    }

    fun setDropdownSchedule(id: Int) {
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, InstructorPermissionApi.GETDATASCHEDULE + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var schedule = arrayListOf<String>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
//                    var data = DropDownSchedule(
//                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_JADWAL_HARIAN"),
//                    )
                    schedule.add(jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_JADWAL_HARIAN"))
                }
//                var data_array: Array<DropDownSchedule> = schedule.toTypedArray()

                val adapter: ArrayAdapter<String> = ArrayAdapter(this,R.layout.item_list_dropdown, schedule)
                binding.edTglIzin.setAdapter(adapter)

                if (!schedule.isEmpty()) {
//                    Toast.makeText(this@JanjiTemuActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
//                    MotionToast.darkToast(
//                        this, "Notification Display!",
//                        "Succesfully get data",
//                        MotionToastStyle.SUCCESS,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(
//                            this,
//                            www.sanju.motiontoast.R.font.helvetica_regular
//                        )
//                    )
                }else {
//                    MotionToast.darkToast(
//                        this, "Notification Display!",
//                        "Failed get data",
//                        MotionToastStyle.SUCCESS,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(
//                            this,
//                            www.sanju.motiontoast.R.font.helvetica_regular
//                        )
//                    )
                }

            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(this@JanjiTemuActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
//                    MotionToast.darkToast(
//                        this,"Notification Display!",
//                        errors.getString("message"),
//                        MotionToastStyle.INFO,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                } catch (e: Exception){
//                    Toast.makeText(this@JanjiTemuActivity, e.message, Toast.LENGTH_SHORT).show()
//                    MotionToast.darkToast(
//                        this,"Notification Display!",
//                        e.message.toString(),
//                        MotionToastStyle.INFO,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun storePermission(id: Int){
        val permisson = InstructorPermission(
            id,
            binding.inputLayoutinstrukturpengganti.getEditText()?.getText().toString(),
            binding.edTglIzin.text.toString(),
            binding.inputLayoutKeterangan.getEditText()?.getText().toString(),
            null,
            null,
            null,
        )

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, InstructorPermissionApi.STOREDATA, Response.Listener { response ->
                val gson = Gson()

                var permission = gson.fromJson(response, InstructorPermission::class.java)
                var resJO = JSONObject(response.toString())
//                val userobj = resJO.getJSONObject("data")

                if(permission != null) {
                    MotionToast.darkColorToast(this,"Notification Success!",
                        "Succesfully Create Permission!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@PermissionCreate, PermissionActivity::class.java)
//                    sharedPreferences.edit()
//                        .putInt("id",userobj.getInt("ID_PEGAWAI"))
//                        .putString("role","MO")
//                        .putString("token",token)
//                        .apply()
                    startActivity(intent)
                }
                else {
                    MotionToast.darkColorToast(this,"Notification Failed!",
                        "Failed Create Permission",
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
                    MotionToast.darkColorToast(this,"Notification Error!",
                        errors.getString("message"),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: java.lang.Exception) {
                    Toast.makeText(this@PermissionCreate, e.message,
                        Toast.LENGTH_LONG).show();
                }
            }) {
                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                    return headers
                }

                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(permisson)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }


}