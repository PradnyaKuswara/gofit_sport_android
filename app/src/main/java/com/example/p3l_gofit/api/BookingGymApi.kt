package com.example.p3l_gofit.api

class BookingGymApi {
    companion object {
        //kos
//        val BASE_URL = "http://192.168.0.106/p3l_GOFIT/public/api/"
        //lux coffee
//        val BASE_URL = "http://192.168.1.13/p3l_GOFIT/public/api/"
        //konrite
//        val BASE_URL = "http://10.0.1.185/p3l_GOFIT/public/api/"

        //link api diganti sesuai kebutuhan
        val BASE_URL = "https://www.gofitsport.my.id/api/"
        val GETDATAHISTORY = BASE_URL + "booking-gym-history"
        val STOREDATA = BASE_URL + "booking-gym"
        val CANCELBOOKING = BASE_URL + "cancel-booking-gym/"
//        val CANCELBOOKING = BASE_URL + "cancel-booking/"
    }
}