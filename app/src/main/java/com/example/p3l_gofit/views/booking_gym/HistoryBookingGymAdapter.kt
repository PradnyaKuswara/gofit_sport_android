package com.example.p3l_gofit.views.booking_gym

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_gofit.R
import com.example.p3l_gofit.models.HistoryBookingGym
import com.example.p3l_gofit.views.booking_class.HistoryBookingActivity
import com.example.p3l_gofit.views.booking_class.HistoryBookingClassAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HistoryBookingGymAdapter(private var historys: List<HistoryBookingGym>,context: Context): RecyclerView.Adapter<HistoryBookingGymAdapter.ViewHolder>() {
    private val context: Context

    init {
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = historys[position]
        holder.tvKodeBookingGym.text = data.KODE_BOOKING_GYM
        holder.tvSlotWaktu.text = "Jam ${data.SLOT_WAKTU_GYM}"
        holder.tvStatusBookingGym.text = "Status: ${data.STATUS_PRESENSI_GYM} - ${data.WAKTU_KONFIRMASI_PRESENSI}"
        if(holder.tvStatusBookingGym.text == "Status: null - null"){
            holder.tvStatusBookingGym.text = "Belum dikonfirmasi"
        }
        holder.tvTanggalBookGym.text = "Tanggal Gym: ${data.TANGGAL_BOOKING_GYM}"
        holder.tvTanggalMelakukanGym.text = "Tanggal Booking: ${data.TANGGAL_MELAKUKAN_BOOKING}"
        holder.iconDel.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin membatalkan booking gym ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Iya"){ _, _ ->
                    if (context is HistoryBookingGymActivity){
                        context.cancelBookingGym(data.KODE_BOOKING_GYM)
                    }
                }
                .show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryBookingGymAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_item_booking_gym, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historys.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKodeBookingGym: TextView
        var tvTanggalBookGym: TextView
        var tvTanggalMelakukanGym: TextView
        var tvSlotWaktu: TextView
        var tvStatusBookingGym: TextView
        var iconDel: ImageButton
        var cvBookGym: CardView

        init {
            tvKodeBookingGym = view.findViewById(R.id.text_kode_gym)
            tvTanggalBookGym = view.findViewById(R.id.text_tanggal_gym)
            tvTanggalMelakukanGym = view.findViewById(R.id.text_tanggal_melakukan_gym)
            tvSlotWaktu = view.findViewById(R.id.text_slot_waktu)
            tvStatusBookingGym = view.findViewById(R.id.text_status_konfirmasi_gym)
            iconDel = view.findViewById(R.id.icon_delete_gym)
            cvBookGym = view.findViewById(R.id.cv_book_gym)
        }

    }


}