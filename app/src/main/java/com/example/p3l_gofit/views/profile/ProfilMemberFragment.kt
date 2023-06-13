package com.example.p3l_gofit.views.profile

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
import com.example.p3l_gofit.LoginFragment
import com.example.p3l_gofit.R
import com.example.p3l_gofit.api.BookingClassApi
import com.example.p3l_gofit.api.ProfilApi
import com.example.p3l_gofit.databinding.FragmentHomeBinding
import com.example.p3l_gofit.databinding.FragmentProfilMemberBinding
import com.example.p3l_gofit.models.DailySchedule
import com.example.p3l_gofit.models.ProfilMember
import com.example.p3l_gofit.views.booking_class.BookingClassAdapter
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets


class ProfilMemberFragment : Fragment() {
    private var _binding: FragmentProfilMemberBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var layoutloading: ConstraintLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profil_member, container, false)
        _binding = FragmentProfilMemberBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        queue = Volley.newRequestQueue(activity)
        var role = sharedPreferences.getString("role",null)
        var status = sharedPreferences.getString("status",null)
        val id = sharedPreferences.getString("id",null)
        layoutloading = view.findViewById(R.id.layout_loading)

        binding.srProfil.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener{
            if (id != null) {
                allData(id)
            }
        })
        if (id != null) {
            allData(id)
        }
    }



    private fun allData(id: String) {
        setLoading(true)
        binding.srProfil.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, ProfilApi.PROFIL_MEMBER + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var member = arrayListOf<ProfilMember>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = ProfilMember(
                        jo.getJSONArray("data").getJSONObject(i).getString("ID_MEMBER"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_MEMBER"),
                        jo.getJSONArray("data").getJSONObject(i).getString("MASA_AKTIVASI"),
                        jo.getJSONArray("data").getJSONObject(i).getDouble("SISA_DEPOSIT_MEMBER"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("DEPO_SISA"),
                        jo.getJSONArray("data").getJSONObject(i).getString("MASA_BERLAKU"),
                    )
                    member.add(data)
                }
                binding.inputNamaMember.setText(jo.getJSONArray("data").getJSONObject(0).getString("NAMA_MEMBER"))
                if(jo.getJSONArray("data").getJSONObject(0).getString("MASA_AKTIVASI") == "null"){
                    binding.inputMasaAktivasi.setText("Belum Aktivasi")
                }else{
                    binding.inputMasaAktivasi.setText(jo.getJSONArray("data").getJSONObject(0).getString("MASA_AKTIVASI"))
                }

                binding.inputSisaDepositUang.setText("Rp."+jo.getJSONArray("data").getJSONObject(0).getDouble("SISA_DEPOSIT_MEMBER").toString())

                var data_array: Array<ProfilMember> = member.toTypedArray()

                val layoutManager = LinearLayoutManager(context as HomeActivity, LinearLayoutManager.HORIZONTAL, false)
                val adapter : ProfilMemberAdapter = ProfilMemberAdapter(member,context as HomeActivity)
                val rvPermission : RecyclerView = requireView().findViewById(R.id.list_profil)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHorizontalScrollBarEnabled(true)
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srProfil.isRefreshing = false
                setLoading(false)

                if (!data_array.isEmpty()) {
//                    Toast.makeText(context, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
//                    MotionToast.darkToast(
//                        context as HomeActivity, "Notification Display!",
//                        "Succesfully get data",
//                        MotionToastStyle.SUCCESS,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(
//                            context as HomeActivity,
//                            www.sanju.motiontoast.R.font.helvetica_regular
//                        )
//                    )
//                    binding.srProfil.isRefreshing = false
                }else {
//                    MotionToast.darkToast(
//                        context as HomeActivity, "Notification Display!",
//                        "Data not found",
//                        MotionToastStyle.SUCCESS,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(
//                            context as HomeActivity,
//                            www.sanju.motiontoast.R.font.helvetica_regular
//                        )
//                    )
//                    binding.srProfil.isRefreshing = false
                }

            }, Response.ErrorListener { error ->
                binding.srProfil.isRefreshing = true
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(context, errors.getString("message"), Toast.LENGTH_SHORT).show()
//                    MotionToast.darkToast(
//                        context as HomeActivity,"Notification Display!",
//                        errors.getString("message"),
//                        MotionToastStyle.INFO,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(context as HomeActivity, www.sanju.motiontoast.R.font.helvetica_regular))
                    binding.srProfil.isRefreshing = false
                } catch (e: Exception){
//                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
//                    MotionToast.darkToast(
//                        context as HomeActivity,"Notification Display!",
//                        e.message.toString(),
//                        MotionToastStyle.INFO,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(context as HomeActivity, www.sanju.motiontoast.R.font.helvetica_regular))
                    binding.srProfil.isRefreshing = false
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
        binding.srProfil.isRefreshing = false
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