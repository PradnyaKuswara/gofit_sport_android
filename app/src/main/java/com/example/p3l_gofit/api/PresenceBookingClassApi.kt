package com.example.p3l_gofit.api

class PresenceBookingClassApi {
    companion object {
        //link api diganti sesuai kebutuhan
        val BASE_URL = "https://gofit-sport.000webhostapp.com/api/"
//        val STOREDATA = BASE_URL + "presence-instructor"
        val GETDATASCHEDULE = BASE_URL + "instructor-schedule-presence/"
        val GETDATAMEMBER = BASE_URL + "member-presence/"
        val UPDATETRANSACTION = BASE_URL + "update-transaction-presence"
    }
}