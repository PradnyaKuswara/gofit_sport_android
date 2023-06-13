package com.example.p3l_gofit.api

class BookingClassApi {
    companion object {
        //kos
//        val BASE_URL = "http://192.168.0.106/p3l_GOFIT/public/api/"
        //lux coffe
//        val BASE_URL = "http://192.168.1.13/p3l_GOFIT/public/api/"
//        val BASE_URL = "http://10.0.1.185/p3l_GOFIT/public/api/"
        //link api diganti sesuai kebutuhan
        val BASE_URL = "https://www.gofitsport.my.id/api/"

        val GETDATASCHEDULE = BASE_URL + "data-daily-schedule/"
        val STOREDATA = BASE_URL + "booking-class"
        val GETDATAHISTORY = BASE_URL + "booking-class-history"
        val CANCELBOOKING = BASE_URL + "cancel-booking/"

        val HISTORYINSTRUCTOR = BASE_URL + "instructor-history"
    }
}