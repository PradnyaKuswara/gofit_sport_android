package com.example.p3l_gofit

class Dokter(var nama: String, var spesialis: String, var tahunMasuk: Int, var hariPraktek: String){

    companion object{
        @JvmField
        var listOfDokter = arrayOf(
            Dokter("Dr. Edward", "Kulit", 2005, "Selasa & Kamis"),
            Dokter("Dr. Michael", "Anak", 2010, "Senin & Selasa"),
            Dokter("Dr. Chris", "Paru-Paru", 2008, "Rabu & Jumat"),
            Dokter("Dr. Joel", "Mata", 2015, "Rabu & Kamis"),
            Dokter("Dr. Johan", "Saraf", 2012, "Senin & Jumat"),
            Dokter("Dr. Mera", "Bedah", 2005, "Selasa & Kamis"),
            Dokter("Dr. Pamela", "Anak", 2010, "Senin & Selasa"),
            Dokter("Dr. Herlina", "THT", 2008, "Rabu & Jumat"),
            Dokter("Dr. Joana", "Kandungan", 2015, "Rabu & Kamis"),
            Dokter("Dr. Lenny", "Kandungan", 2012, "Senin & Jumat"),
        )
    }
}