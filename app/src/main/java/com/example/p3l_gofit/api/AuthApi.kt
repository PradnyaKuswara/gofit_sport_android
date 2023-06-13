package com.example.p3l_gofit.api

class AuthApi {
    companion object {
        //link api diganti sesuai kebutuhan
        val BASE_URL = "https://gofit-sport.000webhostapp.com/api/"

        val LOGIN = BASE_URL + "loginuser"
        val LOGOUT = BASE_URL + "logoutuser"
        val FORGOT = BASE_URL + "forgotpass"
    }
}