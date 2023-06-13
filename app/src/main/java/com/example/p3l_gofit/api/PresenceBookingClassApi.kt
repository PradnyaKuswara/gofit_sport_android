package com.example.p3l_gofit.api

class PresenceBookingClassApi {
    companion object {
        //kos
//        val BASE_URL = "http://192.168.0.106/p3l_GOFIT/public/api/"
        //lux coffee
//        val BASE_URL = "http://192.168.1.13/p3l_GOFIT/public/api/"
        //konrite
//        val BASE_URL = "http://10.0.1.185/p3l_GOFIT/public/api/"

        //link api diganti sesuai kebutuhan
        val BASE_URL = "https://www.gofitsport.my.id/api/"
//        val STOREDATA = BASE_URL + "presence-instructor"
        val GETDATASCHEDULE = BASE_URL + "instructor-schedule-presence/"
        val GETDATAMEMBER = BASE_URL + "member-presence/"
        val UPDATETRANSACTION = BASE_URL + "update-transaction-presence"
    }
}