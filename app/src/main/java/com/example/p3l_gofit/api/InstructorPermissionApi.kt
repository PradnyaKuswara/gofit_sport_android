package com.example.p3l_gofit.api

class InstructorPermissionApi {
    companion object {
        //kos
//        val BASE_URL = "http://192.168.0.106/p3l_GOFIT/public/api/"
        //lux coffee
//        val BASE_URL = "http://192.168.1.13/p3l_GOFIT/public/api/"
        //konrite
//        val BASE_URL = "http://10.0.1.185/p3l_GOFIT/public/api/"

        //link api diganti sesuai kebutuhan
        val BASE_URL = "https://www.gofitsport.my.id/api/"
        val GETDATA = BASE_URL + "data-permission"
        val GETDATASCHEDULE = BASE_URL + "data-schedule/"
        val STOREDATA = BASE_URL + "store-data-permission"
    }
}