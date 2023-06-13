package com.example.p3l_gofit.views.member_presence_class

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_gofit.R
import com.example.p3l_gofit.api.InstructorAttendanceApi
import com.example.p3l_gofit.api.PresenceBookingClassApi
import com.example.p3l_gofit.databinding.ActivityScheduleInstructorBinding
import com.example.p3l_gofit.databinding.ActivityScheduleInstructorPresenceBinding
import com.example.p3l_gofit.models.InstructorSchedule
import com.example.p3l_gofit.views.instructor_attendance.ScheduleInstructorAdapter
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class ScheduleInstructorPresenceActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityScheduleInstructorPresenceBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleInstructorPresenceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("id",0)
        queue = Volley.newRequestQueue(this)

        binding.srScheduleInstructorPresence.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener{allData(id)})
        allData(id)
    }

    private fun allData(id: Int) {
        binding.srScheduleInstructorPresence.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PresenceBookingClassApi.GETDATASCHEDULE + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var schedule = arrayListOf<InstructorSchedule>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = InstructorSchedule(
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_JADWAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("KETERANGAN_JADWAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("HARI_JADWAL"),
                        jo.getJSONArray("data").getJSONObject(i).getInt("ID_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getDouble("TARIF"),
                        null,
                        null
                    )
                    schedule.add(data)
                }
                var data_array: Array<InstructorSchedule> = schedule.toTypedArray()

                val layoutManager = LinearLayoutManager(this)
                val adapter : ScheduleInstructorPresenceAdapter = ScheduleInstructorPresenceAdapter(schedule,this)
                val rvPermission : RecyclerView = findViewById(R.id.list_schedule_instructor_presence)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srScheduleInstructorPresence.isRefreshing = false

                if (!data_array.isEmpty()) {
//                    Toast.makeText(this@ScheduleInstructorActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this, "Notification Display!",
                        jo.getString("message"),
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            this,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                }else {
                    MotionToast.darkToast(
                        this, "Notification Display!",
                        "Data not found",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            this,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                }

            }, Response.ErrorListener { error ->
                binding.srScheduleInstructorPresence.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@ScheduleInstructorPresenceActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Display!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                } catch (e: Exception){
                    Toast.makeText(this@ScheduleInstructorPresenceActivity, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Display!",
                        e.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
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
}