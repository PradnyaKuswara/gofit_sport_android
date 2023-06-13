package com.example.p3l_gofit.views.instructor_attendance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_gofit.R
import com.example.p3l_gofit.models.InstructorSchedule
import com.example.p3l_gofit.views.booking_class.HistoryBookingActivity
import com.example.p3l_gofit.views.booking_gym.HistoryBookingGymActivity
import com.example.p3l_gofit.views.booking_gym.HistoryBookingGymAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ScheduleInstructorAdapter(private var instructors: List<InstructorSchedule>, context: Context): RecyclerView.Adapter<ScheduleInstructorAdapter.ViewHolder>() {
    private val context: Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleInstructorAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_item_instructor_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleInstructorAdapter.ViewHolder, position: Int) {
        val data = instructors[position]
        holder.tvKelas.text = data.NAMA_KELAS
        holder.tvInstruktur.text = data.NAMA_INSTRUKTUR
        holder.tvTanggal.text = data.TANGGAL_JADWAL_HARIAN
        holder.tvHari.text = data.HARI_KELAS
        holder.tvKeterangan.text = data.KETERANGAN_JADWAL_HARIAN
        holder.tvJam.text = "${data.JAM_MULAI} - ${data.JAM_SELESAI}"

        holder.btnStart.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin update jam mulai dan jam selesai kelas ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Iya"){ _, _ ->
                    if (context is ScheduleInstructorActivity){
                        context.store(data.ID_INSTRUKTUR,data.TANGGAL_JADWAL_HARIAN)
                    }
                }
                .show()
        }
    }

    override fun getItemCount(): Int {
        return instructors.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKelas: TextView
        var tvInstruktur: TextView
        var tvKeterangan: TextView
        var tvHari: TextView
        var tvTanggal: TextView
        var tvJam: TextView
        var cvSchedule: CardView
        var btnStart: Button


        init {
            tvKelas = view.findViewById(R.id.text_kelas_instructor)
            tvInstruktur = view.findViewById(R.id.text_instruktur_instructor)
            tvKeterangan = view.findViewById(R.id.text_keterangan_kelas_instructor)
            tvHari = view.findViewById(R.id.tv_hari_instructor)
            tvTanggal = view.findViewById(R.id.text_tanggal_kelas_instructor)
            tvJam = view.findViewById(R.id.tv_jam)
            cvSchedule = view.findViewById(R.id.cv_schedule_instructor)
            btnStart = view.findViewById(R.id.btn_start)
        }

    }
}