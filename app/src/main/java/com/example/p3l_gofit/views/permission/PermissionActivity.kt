package com.example.p3l_gofit.views.permission

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
import com.example.p3l_gofit.Dokter
import com.example.p3l_gofit.R
import com.example.p3l_gofit.api.InstructorPermissionApi
import com.example.p3l_gofit.databinding.ActivityPermissionBinding
import com.example.p3l_gofit.models.InstructorPermission
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

class PermissionActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityPermissionBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("id",-1)
        queue = Volley.newRequestQueue(this)

        setMonthDropdown()
        setYearDropdown()

        binding.srPermission.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener
        {
            if (id != 0 && binding.layoutBulan.getEditText()?.getText().toString().isNullOrEmpty() && binding.layoutTahun.getEditText()?.getText().toString().isNullOrEmpty()) {
                var bulan = LocalDateTime.now().month
                var tahun = LocalDateTime.now().year
                binding.textBulanTahun.setText("${bulan} - ${tahun}")
                allData(id,bulan.toString(),tahun.toString())
            }else if(id !=0){
                binding.textBulanTahun.setText("${binding.layoutBulan.getEditText()?.getText().toString()} - ${binding.layoutTahun.getEditText()?.getText().toString()}")
                allData(id,binding.layoutBulan.getEditText()?.getText().toString(),binding.layoutTahun.getEditText()?.getText().toString())
            }
        })
        if (id != 0 && binding.layoutBulan.getEditText()?.getText().toString().isNullOrEmpty() && binding.layoutTahun.getEditText()?.getText().toString().isNullOrEmpty()) {
            var bulan = LocalDateTime.now().month
            var tahun = LocalDateTime.now().year
            binding.textBulanTahun.setText("${bulan} - ${tahun}")
            allData(id,bulan.toString(),tahun.toString())
        }
        binding.btnSearch.setOnClickListener {
            if (id != 0) {
                binding.textBulanTahun.setText("${binding.layoutBulan.getEditText()?.getText().toString()} - ${binding.layoutTahun.getEditText()?.getText().toString()}")
                allData(id,binding.layoutBulan.getEditText()?.getText().toString(),binding.layoutTahun.getEditText()?.getText().toString())
            }
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this@PermissionActivity, PermissionCreate::class.java)
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

    private fun allData(id: Int, bulan:String, tahun: String) {
        binding.srPermission.isRefreshing = true
        val urlstruct: String = "?ID_INSTRUKTUR=${id}&BULAN=${bulan}&TAHUN=${tahun}"
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, InstructorPermissionApi.GETDATA + urlstruct, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var permission = arrayListOf<InstructorPermission>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = InstructorPermission(
                        jo.getJSONArray("data").getJSONObject(i).getInt("ID_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_INSTRUKTUR_PENGGANTI"),
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_IZIN_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("KETERANGAN_IZIN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_MELAKUKAN_IZIN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("STATUS_IZIN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_KONFIRMASI_IZIN")
                    )
                    permission.add(data)
                }
                var data_array: Array<InstructorPermission> = permission.toTypedArray()

                val layoutManager = LinearLayoutManager(this)
                val adapter : PermissionAdapter = PermissionAdapter(permission,this)
                val rvPermission : RecyclerView = findViewById(R.id.list_permission)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srPermission.isRefreshing = false

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
                binding.srPermission.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(this@JanjiTemuActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Display!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                } catch (e: Exception){
//                    Toast.makeText(this@JanjiTemuActivity, e.message, Toast.LENGTH_SHORT).show()
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