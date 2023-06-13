package com.example.p3l_gofit.views.profile

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_gofit.HomeActivity
import com.example.p3l_gofit.R
import com.example.p3l_gofit.api.ProfilApi
import com.example.p3l_gofit.databinding.FragmentProfilInstructorBinding
import com.example.p3l_gofit.databinding.FragmentProfilMemberBinding
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets


class ProfilInstructorFragment : Fragment() {
    private var _binding: FragmentProfilInstructorBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var layoutloading: ConstraintLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profil_instructor, container, false)
        _binding = FragmentProfilInstructorBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        queue = Volley.newRequestQueue(activity)
        var role = sharedPreferences.getString("role",null)
        var status = sharedPreferences.getString("status",null)
        val id = sharedPreferences.getInt("id",0)
        layoutloading = view.findViewById(R.id.layout_loading)

        getInstructorByid(id)
    }

    private fun getInstructorByid(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, ProfilApi.PROFIL_INSTRUCTOR + id, Response.Listener { response ->
                // val gson = Gson()
                // val mahasiswa = gson.fromJson(response, Mahasiswa::class.java)

                var joUser = JSONObject(response.toString())
                val userdata = joUser.getJSONObject("data")

                binding.inputNamaInstruktur.setText(userdata.getString("NAMA_INSTRUKTUR"))
                binding.inputEmailInstruktur.setText(userdata.getString("EMAIL_INSTRUKTUR"))
                binding.inputAlamatInstruktur.setText(userdata.getString("ALAMAT_INSTRUKTUR"))
                binding.inputNomorTelepon.setText(userdata.getString("TELEPON_INSTRUKTUR"))
                binding.inputWaktuTerlambat.setText(userdata.getString("JUMLAH_TERLAMBAT"))

                setLoading(false)
//                Toast.makeText(activity, "Data User berhasil diambil!", Toast.LENGTH_SHORT).show()
//                MotionToast.darkColorToast(
//                    context as Activity,"Notification Display!",
//                    "Data User Berhasil Ditampilkan!!",
//                    MotionToastStyle.SUCCESS,
//                    MotionToast.GRAVITY_BOTTOM,
//                    MotionToast.LONG_DURATION,
//                    ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        activity,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    MotionToast.darkColorToast(
//                        context as Activity,"Notification Profil!",
//                        errors.getString("message"),
//                        MotionToastStyle.INFO,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: Exception) {
//                    MotionToast.darkColorToast(
//                        context as Activity,"Notification Profil!",
//                        e.message.toString(),
//                        MotionToastStyle.INFO,
//                        MotionToast.GRAVITY_BOTTOM,
//                        MotionToast.LONG_DURATION,
//                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }
            }) {
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