package com.example.p3l_gofit.views.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.example.p3l_gofit.api.PresenceBookingClassApi
import com.example.p3l_gofit.databinding.FragmentHistoryInstructorBinding
import com.example.p3l_gofit.databinding.FragmentLoginBinding
import com.example.p3l_gofit.models.InstructorSchedule
import com.example.p3l_gofit.views.member_presence_class.ScheduleInstructorPresenceAdapter
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime


class HistoryInstructorFragment : Fragment() {
    private var _binding: FragmentHistoryInstructorBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var layoutloading: ConstraintLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_history_instructor, container, false)
        _binding = FragmentHistoryInstructorBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        queue = Volley.newRequestQueue(activity)
        layoutloading = view.findViewById(R.id.layout_loading)

        setMonthDropdown()
        setYearDropdown()

        val id = sharedPreferences.getInt("id",0)

        binding.srScheduleInstructorPresence.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener{
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
    }

    private fun setMonthDropdown()
    {
        var month = arrayListOf<String>()
        for(i in 0 until 12) {
            var temp= LocalDateTime.now().withMonth(i+1).month
            month.add(temp.toString().capitalize())
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter(context as HomeActivity,R.layout.item_list_dropdown, month)
        binding.edBulan.setAdapter(adapter)
    }

    private fun setYearDropdown()
    {
        var month = arrayListOf<String>()
        for(i in 0 until 3) {
            var temp= LocalDateTime.now().minusYears((i).toLong()).year
            month.add(temp.toString().capitalize())
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter(context as HomeActivity,R.layout.item_list_dropdown, month)
        binding.edTahun.setAdapter(adapter)
    }

    private fun allData(id: Int, bulan: String, tahun: String) {
        setLoading(true)
        binding.srScheduleInstructorPresence.isRefreshing = true
        val urlstruct: String = "?ID_INSTRUKTUR=${id}&BULAN=${bulan}&TAHUN=${tahun}"
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, BookingClassApi.HISTORYINSTRUCTOR + urlstruct, Response.Listener { response ->
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

                val layoutManager = LinearLayoutManager(context as HomeActivity)
                val adapter : ScheduleInstructorPresenceAdapter = ScheduleInstructorPresenceAdapter(schedule,context as HomeActivity)
                val rvPermission : RecyclerView = requireView().findViewById(R.id.list_schedule_instructor_presence)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srScheduleInstructorPresence.isRefreshing = false
                setLoading(false)

                if (!data_array.isEmpty()) {
//                    Toast.makeText(this@ScheduleInstructorActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        context as HomeActivity, "Notification Display!",
                        jo.getString("message"),
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            context as HomeActivity,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                }else {
                    MotionToast.darkToast(
                        context as HomeActivity, "Notification Display!",
                        "Data not found",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            context as HomeActivity,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                }

            }, Response.ErrorListener { error ->
                setLoading(false)
                binding.srScheduleInstructorPresence.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(this@ScheduleInstructorPresenceActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        context as HomeActivity,"Notification Display!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as HomeActivity, www.sanju.motiontoast.R.font.helvetica_regular))
                } catch (e: Exception){
//                    Toast.makeText(this@ScheduleInstructorPresenceActivity, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        context as HomeActivity,"Notification Display!",
                        e.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as HomeActivity, www.sanju.motiontoast.R.font.helvetica_regular))
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

    private fun setLoading(isLoading: Boolean) {
        if(isLoading) {
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutloading!!.visibility = View.VISIBLE
        }else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutloading!!.visibility = View.INVISIBLE
        }
    }

}