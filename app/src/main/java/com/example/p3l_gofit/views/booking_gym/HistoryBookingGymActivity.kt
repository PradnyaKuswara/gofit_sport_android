package com.example.p3l_gofit.views.booking_gym

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_gofit.HomeActivity
import com.example.p3l_gofit.R
import com.example.p3l_gofit.api.BookingClassApi
import com.example.p3l_gofit.api.BookingGymApi
import com.example.p3l_gofit.databinding.ActivityHistoryBookingBinding
import com.example.p3l_gofit.databinding.ActivityHistoryBookingGymBinding
import com.example.p3l_gofit.models.HistoryBookingClass
import com.example.p3l_gofit.models.HistoryBookingGym
import com.example.p3l_gofit.views.booking_class.BookingClassActivity
import com.example.p3l_gofit.views.booking_class.HistoryBookingClassAdapter
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

class HistoryBookingGymActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityHistoryBookingGymBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBookingGymBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)

        val id = sharedPreferences.getString("id",null)
        queue = Volley.newRequestQueue(this)

        setMonthDropdown()
        setYearDropdown()

        binding.srBookingGym.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener{
            if (id != null && binding.layoutBulan.getEditText()?.getText().toString().isNullOrEmpty() && binding.layoutTahun.getEditText()?.getText().toString().isNullOrEmpty()) {
                var bulan = LocalDateTime.now().month
                var tahun = LocalDateTime.now().year
                binding.textBulanTahun.setText("${bulan} - ${tahun}")
                allData(id,bulan.toString(),tahun.toString())
            }else if(id !=null){
                binding.textBulanTahun.setText("${binding.layoutBulan.getEditText()?.getText().toString()} - ${binding.layoutTahun.getEditText()?.getText().toString()}")
                allData(id,binding.layoutBulan.getEditText()?.getText().toString(),binding.layoutTahun.getEditText()?.getText().toString())
            }
        })
        if (id != null && binding.layoutBulan.getEditText()?.getText().toString().isNullOrEmpty() && binding.layoutTahun.getEditText()?.getText().toString().isNullOrEmpty()) {
            var bulan = LocalDateTime.now().month
            var tahun = LocalDateTime.now().year
            binding.textBulanTahun.setText("${bulan} - ${tahun}")
            allData(id,bulan.toString(),tahun.toString())
        }
        binding.btnSearch.setOnClickListener {
            if (id != null) {
                binding.textBulanTahun.setText("${binding.layoutBulan.getEditText()?.getText().toString()} - ${binding.layoutTahun.getEditText()?.getText().toString()}")
                allData(id,binding.layoutBulan.getEditText()?.getText().toString(),binding.layoutTahun.getEditText()?.getText().toString())
            }
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this@HistoryBookingGymActivity, BookingGymActivity::class.java)
//            sharedPreferences.edit()
//                .putString("booking","yes")
//                .apply()
            startActivity(intent)
        }
    }

    private fun setMonthDropdown()
    {
        var month = arrayListOf<String>()
        for(i in 0 until 12) {
            var temp= LocalDateTime.now().withMonth(i+1).month
            month.add(temp.toString().capitalize())
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter(this,R.layout.item_list_dropdown, month)
        binding.edBulan.setAdapter(adapter)
    }

    private fun setYearDropdown()
    {
        var month = arrayListOf<String>()
        for(i in 0 until 3) {
            var temp= LocalDateTime.now().minusYears((i).toLong()).year
            month.add(temp.toString().capitalize())
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter(this,R.layout.item_list_dropdown, month)
        binding.edTahun.setAdapter(adapter)
    }

    private fun allData(id: String, bulan: String, tahun: String) {
        binding.srBookingGym.isRefreshing = true
        val urlstruct: String = "?ID_MEMBER=${id}&BULAN=${bulan}&TAHUN=${tahun}"
        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET,
                BookingGymApi.GETDATAHISTORY + urlstruct,
                Response.Listener { response ->
                    var jo = JSONObject(response.toString())
                    var history = arrayListOf<HistoryBookingGym>()
                    var id: Int = jo.getJSONArray("data").length()

                    for (i in 0 until id) {
                        var data = HistoryBookingGym(
                            jo.getJSONArray("data").getJSONObject(i).getString("KODE_BOOKING_GYM"),
                            jo.getJSONArray("data").getJSONObject(i).getString("ID_MEMBER"),
                            jo.getJSONArray("data").getJSONObject(i).getString("SLOT_WAKTU_GYM"),
                            jo.getJSONArray("data").getJSONObject(i)
                                .getString("TANGGAL_BOOKING_GYM"),
                            jo.getJSONArray("data").getJSONObject(i)
                                .getString("TANGGAL_MELAKUKAN_BOOKING"),
                            jo.getJSONArray("data").getJSONObject(i)
                                .getString("WAKTU_KONFIRMASI_PRESENSI"),
                            jo.getJSONArray("data").getJSONObject(i)
                                .getString("STATUS_PRESENSI_GYM"),
                        )
                        history.add(data)
                    }
                    var data_array: Array<HistoryBookingGym> = history.toTypedArray()

                    val layoutManager = LinearLayoutManager(this)
                    val adapter: HistoryBookingGymAdapter = HistoryBookingGymAdapter(history, this)
                    val rvPermission: RecyclerView = findViewById(R.id.list_booking_gym)

                    rvPermission.layoutManager = layoutManager
                    rvPermission.setHasFixedSize(true)
                    rvPermission.adapter = adapter

                    binding.srBookingGym.isRefreshing = false

                    if (!data_array.isEmpty()) {
//                    Toast.makeText(this@JanjiTemuActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
                        MotionToast.darkToast(
                            this, "Notification Display!",
                            "Succesfully get data",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(
                                this,
                                www.sanju.motiontoast.R.font.helvetica_regular
                            )
                        )
                    } else {
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

                },
                Response.ErrorListener { error ->
                    binding.srBookingGym.isRefreshing = true
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
//                    Toast.makeText(this@JanjiTemuActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                        MotionToast.darkToast(
                            this, "Notification Display!",
                            errors.getString("message"),
                            MotionToastStyle.INFO,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(
                                this,
                                www.sanju.motiontoast.R.font.helvetica_regular
                            )
                        )
                        binding.srBookingGym.isRefreshing = false
                    } catch (e: Exception) {
//                    Toast.makeText(this@JanjiTemuActivity, e.message, Toast.LENGTH_SHORT).show()
                        MotionToast.darkToast(
                            this, "Notification Display!",
                            e.message.toString(),
                            MotionToastStyle.INFO,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(
                                this,
                                www.sanju.motiontoast.R.font.helvetica_regular
                            )
                        )
                        binding.srBookingGym.isRefreshing = false
                    }

                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token", null);
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    fun cancelBookingGym(id: String) {
//        binding.srBooking.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, BookingGymApi.CANCELBOOKING + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())
//                var history = arrayListOf<HistoryBookingClass>()

                if (jo.getJSONObject("data") != null) {
//                    Toast.makeText(this@JanjiTemuActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this, "Notification Booking!",
                        "Succesfully cancel booking gym",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            this,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                    val intent = Intent(this@HistoryBookingGymActivity, HomeActivity::class.java)
                    startActivity(intent)
                }else {
                    MotionToast.darkToast(
                        this, "Notification Booking!",
                        "Failed cancel booking gym",
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
                binding.srBookingGym.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(this@JanjiTemuActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Booking!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    binding.srBookingGym.isRefreshing = false
                } catch (e: Exception){
//                    Toast.makeText(this@JanjiTemuActivity, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Booking!",
                        e.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    binding.srBookingGym.isRefreshing = false
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