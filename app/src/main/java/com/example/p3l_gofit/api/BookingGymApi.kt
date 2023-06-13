package com.example.p3l_gofit.api

class BookingGymApi {
    companion object {
        //link api diganti sesuai kebutuhan
        val BASE_URL = "https://gofit-sport.000webhostapp.com/api/"
        val GETDATAHISTORY = BASE_URL + "booking-gym-history"
        val STOREDATA = BASE_URL + "booking-gym"
        val CANCELBOOKING = BASE_URL + "cancel-booking-gym/"
//        val CANCELBOOKING = BASE_URL + "cancel-booking/"
    }
}