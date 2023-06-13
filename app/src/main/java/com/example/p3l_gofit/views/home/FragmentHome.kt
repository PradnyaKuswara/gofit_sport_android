package com.example.p3l_gofit.views.home

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
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
import com.example.p3l_gofit.databinding.FragmentHomeBinding
import com.example.p3l_gofit.models.DailySchedule
import com.example.p3l_gofit.views.booking_class.BookingClassAdapter
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class FragmentHome : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var layoutloading: ConstraintLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        queue = Volley.newRequestQueue(activity)
        layoutloading = view.findViewById(R.id.layout_loading)
        var role = sharedPreferences.getString("role",null)
        var status = sharedPreferences.getString("status",null)
        if(!(status.isNullOrEmpty())){
            binding.textHome.setText("Hello "+ sharedPreferences.getString("name",null))
        }else {
            binding.textHome.setText("Hello user, Silahkan login terlebih dahulu!")
        }
//        binding.srClass.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener{allData()})
        allData()
    }

    private fun allData() {
//        binding.srClass.isRefreshing = true
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, BookingClassApi.GETDATASCHEDULE, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var schedule = arrayListOf<DailySchedule>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = DailySchedule(
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_JADWAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("KETERANGAN_JADWAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("HARI_JADWAL"),
                        jo.getJSONArray("data").getJSONObject(i).getInt("ID_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getDouble("TARIF"),
                    )
                    schedule.add(data)
                }
                var data_array: Array<DailySchedule> = schedule.toTypedArray()

                val layoutManager = LinearLayoutManager(context as HomeActivity)
                val adapter : BookingClassAdapter = BookingClassAdapter(schedule,context as HomeActivity)
                val rvPermission : RecyclerView = requireView().findViewById(R.id.list_class_fragment)

                rvPermission.layoutManager = layoutManager
                rvPermission.setVerticalScrollBarEnabled(true)
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                setLoading(false)
//                binding.srClass.isRefreshing = false

                if (!data_array.isEmpty()) {
//                    Toast.makeText(this@JanjiTemuActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
//                    MotionToast.darkToast(
//                        context as Activity, "Notification Display!",
//                        "Succesfully get data",
//                        MotionToastStyle.SUCCESS,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(
//                            context as Activity,
//                            www.sanju.motiontoast.R.font.helvetica_regular
//                        )
//                    )
                }else {
//                    MotionToast.darkToast(
//                        context as Activity, "Notification Display!",
//                        "Data not found",
//                        MotionToastStyle.SUCCESS,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(
//                            context as Activity,
//                            www.sanju.motiontoast.R.font.helvetica_regular
//                        )
//                    )
                }

            }, Response.ErrorListener { error ->
//                binding.srClass.isRefreshing = true
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(this@JanjiTemuActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
//                    MotionToast.darkToast(
//                        context as Activity,"Notification Display!",
//                        errors.getString("message"),
//                        MotionToastStyle.INFO,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                } catch (e: Exception){
//                    Toast.makeText(this@JanjiTemuActivity, e.message, Toast.LENGTH_SHORT).show()
//                    MotionToast.darkToast(
//                        context as Activity,"Notification Display!",
//                        e.message.toString(),
//                        MotionToastStyle.INFO,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
//                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
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