package com.example.p3l_gofit.views.booking_class

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_gofit.R
import com.example.p3l_gofit.models.DailySchedule
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BookingClassAdapter(private var dailyschedules: List<DailySchedule>,context: Context): RecyclerView.Adapter<BookingClassAdapter.ViewHolder>()
{
    private val context: Context


    init {
        this.context = context

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_item_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dailyschedules[position]
        val preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)

        holder.tvKelas.text = data.NAMA_KELAS
        holder.tvTanggalKelas.text = data.TANGGAL_JADWAL_HARIAN
        holder.tvInstruktur.text = data.NAMA_INSTRUKTUR
        holder.tvKeteranganKelas.text = data.KETERANGAN_JADWAL_HARIAN

        holder.tvHari.text = data.HARI_KELAS
        holder.tvTarif.text = "Rp.${data.TARIF.toString()}"

        holder.cvKelas.setOnClickListener {
            if(holder.tvKeteranganKelas.text == "Libur") {
                Toast.makeText(context,"Kelas ditiadakan", Toast.LENGTH_SHORT).show()
            }else {
//                val i = Intent(context, BookingClassCreate::class.java)
//                i.putExtra("id_tanggal", data.TANGGAL_JADWAL_HARIAN)
//                i.putExtra("id_kelas",data.ID_KELAS)
//                if(context is BookingClassActivity){
//                    ContextCompat.startActivity(context,i,null)
//                }
                if(!(preferences.getString("booking",null).isNullOrEmpty())){
                    val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
                    materialAlertDialogBuilder.setTitle("Konfirmasi")
                        .setMessage("Apakah anda yakin ingin booking kelas ini?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Iya"){ _, _ ->
                            if (context is BookingClassActivity){
                                context.getSharedPreferences("session",Context.MODE_PRIVATE).getString("id",null)
                                    ?.let { it1 -> context.bookingClass(it1,data.ID_KELAS,data.TANGGAL_JADWAL_HARIAN) }
                            }
                        }
                        .show()
                }else if((preferences.getString("status",null).isNullOrEmpty())){
                    Toast.makeText(context,"Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                }else{
//                    Toast.makeText(context,"Silahkan pilih menu booking", Toast.LENGTH_SHORT).show()
                }
            }
//            preferences.edit()
//                .putString("booking",null)
//                .apply()
        }
    }

    override fun getItemCount(): Int {
        return dailyschedules.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKelas: TextView
        var tvInstruktur: TextView
        var tvKeteranganKelas: TextView
        var tvTanggalKelas: TextView
        var tvHari: TextView
        var tvTarif: TextView
        var cvKelas: CardView

        init {
            tvKelas = view.findViewById(R.id.text_kelas)
            tvInstruktur = view.findViewById(R.id.text_instruktur)
            tvKeteranganKelas = view.findViewById(R.id.text_keterangan_kelas)
            tvTanggalKelas = view.findViewById(R.id.text_tanggal_kelas)
            tvHari = view.findViewById(R.id.tv_hari)
            tvTarif = view.findViewById(R.id.text_tarif)
            cvKelas = view.findViewById(R.id.cv_class)
        }
    }
}