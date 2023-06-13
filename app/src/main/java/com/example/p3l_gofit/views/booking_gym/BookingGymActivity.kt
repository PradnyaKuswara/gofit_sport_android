package com.example.p3l_gofit.views.booking_gym

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import com.example.p3l_gofit.R
import com.example.p3l_gofit.api.BookingGymApi
import com.example.p3l_gofit.api.InstructorPermissionApi
import com.example.p3l_gofit.databinding.ActivityBookingClassBinding
import com.example.p3l_gofit.databinding.ActivityBookingGymBinding
import com.example.p3l_gofit.models.HistoryBookingGym
import com.example.p3l_gofit.models.InstructorPermission
import com.example.p3l_gofit.views.permission.PermissionActivity
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class BookingGymActivity : AppCompatActivity() {
    companion object{
        private val SLOT_LIST = arrayOf(
            "7-9",
            "9-11",
            "11-13",
            "13-15",
            "15-17",
            "17-19",
            "19-21")
    }
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityBookingGymBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingGymBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)

        val id = sharedPreferences.getString("id",null)
        queue = Volley.newRequestQueue(this)

        setExposedDropDownMenu()

        val cal = Calendar.getInstance()
        val myYear = cal.get(Calendar.YEAR)
        val myMonth = cal.get(Calendar.MONTH)
        val myDay = cal.get(Calendar.DAY_OF_MONTH)

        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)

        binding.edTglIzin.setOnFocusChangeListener { view, b ->
            val datePicker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    var date: String = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                    binding.edTglIzin.setText(date)
                }, myYear, myMonth, myDay
            )
            if (b) {
                datePicker.show()
            } else {
                datePicker.hide()
            }
        }

        binding.btnStore.setOnClickListener {
            if (id != null) {
                storeBookingGym(id)
            }
        }
    }

    fun setExposedDropDownMenu(){
        val adapterslot: ArrayAdapter<String> = ArrayAdapter<String>(this,
            R.layout.item_list_dropdown, SLOT_LIST)
        binding.edSlotWaktu.setAdapter(adapterslot)
    }

    private fun storeBookingGym(id: String){
        val booking = HistoryBookingGym(
            "",
            id,
            binding.layoutSlotWaktu.getEditText()?.getText().toString(),
            binding.layoutTanggal.getEditText()?.getText().toString(),
            null,
            null,
            null,
        )

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, BookingGymApi.STOREDATA, Response.Listener { response ->
                val gson = Gson()
                var booking_gym = gson.fromJson(response, HistoryBookingGym::class.java)
//                var resJO = JSONObject(response.toString())
//                val userobj = resJO.getJSONObject("data")

                if(booking_gym!= null) {
                    MotionToast.darkColorToast(this,"Notification Success!",
                        "Succesfully Create Booking Gym!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@BookingGymActivity, HistoryBookingGymActivity::class.java)
//                    sharedPreferences.edit()
//                        .putInt("id",userobj.getInt("ID_PEGAWAI"))
//                        .putString("role","MO")
//                        .putString("token",token)
//                        .apply()
                    startActivity(intent)
                }
                else {
                    MotionToast.darkColorToast(this,"Notification Failed!",
                        "Failed Create Booking Gym",
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
                    Toast.makeText(this@BookingGymActivity, e.message,
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
                    val requestBody = gson.toJson(booking)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}