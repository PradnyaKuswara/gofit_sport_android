package com.example.p3l_gofit.api

class AuthApi {
    companion object {
        //kos
//        val BASE_URL = "http://192.168.0.106/p3l_GOFIT/public/api/" kos
        //lux coffe
//        val BASE_URL = "http://192.168.1.13/p3l_GOFIT/public/api/" lux coffee
        //konrite
//        val BASE_URL = "http://10.0.1.185/p3l_GOFIT/public/api/"
        //link api diganti sesuai kebutuhan
        val BASE_URL = "https://www.gofitsport.my.id/api/"

        val LOGIN = BASE_URL + "loginuser"
        val LOGOUT = BASE_URL + "logoutuser"
        val FORGOT = BASE_URL + "forgotpass"
    }
}