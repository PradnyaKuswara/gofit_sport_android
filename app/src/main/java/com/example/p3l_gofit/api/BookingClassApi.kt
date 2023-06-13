package com.example.p3l_gofit.api

class BookingClassApi {
    companion object {
        val BASE_URL = "https://gofit-sport.000webhostapp.com/api/"

        val GETDATASCHEDULE = BASE_URL + "data-daily-schedule/"
        val STOREDATA = BASE_URL + "booking-class"
        val GETDATAHISTORY = BASE_URL + "booking-class-history"
        val CANCELBOOKING = BASE_URL + "cancel-booking/"

        val HISTORYINSTRUCTOR = BASE_URL + "instructor-history"
    }
}