package com.example.p3l_gofit.views.profile

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.p3l_gofit.HomeActivity
import com.example.p3l_gofit.R
import com.example.p3l_gofit.databinding.FragmentProfilBinding
import com.example.p3l_gofit.databinding.FragmentProfilMemberBinding


class ProfilFragment : Fragment() {
    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profil, container, false)
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        queue = Volley.newRequestQueue(activity)
        var role = sharedPreferences.getString("role",null)
        var status = sharedPreferences.getString("status",null)

        if(role == "instructor" && status == "logged"){
            transitionFragment(ProfilInstructorFragment())
        }

        if(role == "member" && status == "logged"){
            transitionFragment(ProfilMemberFragment())
        }

    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.layout_fragment, fragment)
            .addToBackStack(null).commit()
        transition.hide(ProfilFragment())
    }


}