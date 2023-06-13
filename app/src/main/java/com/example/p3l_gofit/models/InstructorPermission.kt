package com.example.p3l_gofit.models

class InstructorPermission(
    var ID_INSTRUKTUR: Int,
    var NAMA_INSTRUKTUR_PENGGANTI: String?,
    var TANGGAL_IZIN_INSTRUKTUR: String,
    var KETERANGAN_IZIN: String,
    var TANGGAL_MELAKUKAN_IZIN: String?,
    var STATUS_IZIN: String?,
    var TANGGAL_KONFIRMASI_IZIN: String?,
) {
}