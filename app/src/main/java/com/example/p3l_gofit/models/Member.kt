package com.example.p3l_gofit.models

class Member(
    var NAMA_MEMBER: String,
    var ALAMAT_MEMBER: String,
    var TANGGAL_LAHIR_MEMBER: String?,
    var TELEPON_MEMBER: String,
    var USIA_MEMBER:Int,
    var JENIS_KELAMIN_MEMBER: String,
    var MASA_AKTIVASI: String?,
    var SISA_DEPOSIT_MEMBER: Float,
    var SISA_DEPOSIT_KELAS: Int,
    var EMAIL_MEMBER: String,
    var password: String) {
}