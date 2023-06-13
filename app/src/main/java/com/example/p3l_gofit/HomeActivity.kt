package com.example.p3l_gofit

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.p3l_gofit.views.profile.ProfilMemberFragment
import com.example.p3l_gofit.views.home.FragmentHome
import com.example.p3l_gofit.views.home.FragmentHomeInstructor
import com.example.p3l_gofit.views.home.FragmentHomeMember
import com.example.p3l_gofit.views.home.FragmentHomeMo
import com.example.p3l_gofit.views.profile.HistoryInstructorFragment
import com.example.p3l_gofit.views.profile.ProfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import nl.joery.animatedbottombar.AnimatedBottomBar

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
//    private lateinit var  navigationBottom : BottomNavigationView
    private lateinit var  navigationBottom : AnimatedBottomBar
    private var temp: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        getSupportActionBar()?.getThemedContext();

        setTitle("GOFIT")
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)
        var role = sharedPreferences.getString("role",null)
        var status = sharedPreferences.getString("status",null)

//        if(role.isNullOrEmpty() && status.isNullOrEmpty()){
//            changeFragment(FragmentHome())
//            temp = FragmentHome()
//        }
//
//        if(role == "MO" && status == "logged") {
//            changeFragment(FragmentHomeMo())
//            temp = FragmentHomeMo()
//        }
//        if(role == "member" && status == "logged"){
//            changeFragment(FragmentHomeMember())
//            temp = FragmentHomeMember()
//        }
//        if(role == "instructor" && status == "logged"){
//            changeFragment(FragmentHomeInstructor())
//            temp = FragmentHomeInstructor()
//        }

        changeFragment(FragmentHome())

        init()
        if(status.isNullOrEmpty() ){
//            navigationBottom?.menu?.findItem(R.id.Menu)?.setVisible(false)
//            navigationBottom.setTabEnabledById(R.id.Menu,false)
            navigationBottom.removeTabById(R.id.Menu)
        }else {
//            navigationBottom?.menu?.findItem(R.id.Login)?.setVisible(false)
//            navigationBottom.setTabEnabledById(R.id.Login, false)
            navigationBottom.removeTabById(R.id.Login)

        }
        if(status == "logged" && role == "MO" || status.isNullOrEmpty()){
//            navigationBottom.menu .findItem(R.id.Profil)?.setVisible(false)
//            navigationBottom.setTabEnabledById(R.id.Profil,false)
            navigationBottom.removeTabById(R.id.Profile)
        }else {
//            navigationBottom.setTabEnabledById(R.id.Profil,true)
        }

        if(status == "logged" && role != "instructor" || status.isNullOrEmpty()){
            navigationBottom.removeTabById(R.id.History)
        }
        navListener()

    }

    private fun init(){
        navigationBottom = findViewById(R.id.botNavigation)
    }

    fun changeFragment(fragment: Fragment?) {
        if (fragment != null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment, fragment)
                .commit()
        }
    }

//    private fun navListener() {
//        navigationBottom?.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.Home -> {
//                    changeFragment(FragmentHome())
//                    return@setOnItemSelectedListener true
//                }
//                R.id.Login -> {
//                    changeFragment(LoginFragment())
//                    return@setOnItemSelectedListener true
//                }
//                R.id.Menu -> {
//                    changeFragment(LoginFragment())
//                    return@setOnItemSelectedListener true
//                }
//                R.id.Profil -> {
//                    changeFragment(ProfilFragment())
//                    return@setOnItemSelectedListener true
//                }
//            }
//            false
//        }
//    }
private fun navListener() {
    navigationBottom.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
        override fun onTabSelected(
            lastIndex: Int,
            lastTab: AnimatedBottomBar.Tab?,
            newIndex: Int,
            newTab: AnimatedBottomBar.Tab
        ) {
            when(newIndex) {
                0 -> changeFragment(FragmentHome())
                1 -> changeFragment(LoginFragment())
                2 -> changeFragment(ProfilFragment())
                3 -> changeFragment(HistoryInstructorFragment())
                else -> changeFragment(ProfilFragment())
            }
            Log.d("bottom_bar", "Selected index: $newIndex, title: ${newTab.title}")
        }

        override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
            Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
        }
    })
}
    fun getSharedPreferences() : SharedPreferences {
        return sharedPreferences
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Tolong Konfirmasi")
            setMessage("Apakah anda yakin ingin keluar?")

            setPositiveButton("Iya") { _, _ ->
                moveTaskToBack(true)
                android.os.Process.killProcess(android.os.Process.myPid())
                System.exit(1)

            }

            setNegativeButton("Tidak"){_, _ ->
            }

            setCancelable(true)
        }.create().show()
    }
}