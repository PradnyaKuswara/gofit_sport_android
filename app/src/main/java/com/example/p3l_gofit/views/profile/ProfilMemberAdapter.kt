package com.example.p3l_gofit.views.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_gofit.R
import com.example.p3l_gofit.models.ProfilMember
import com.example.p3l_gofit.views.booking_class.HistoryBookingActivity
import com.example.p3l_gofit.views.booking_class.HistoryBookingClassAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfilMemberAdapter(private var profils: List<ProfilMember>,context: Context): RecyclerView.Adapter<ProfilMemberAdapter.ViewHolder>() {
    private val context: Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilMemberAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_item_profil_member, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfilMemberAdapter.ViewHolder, position: Int) {
        val data = profils[position]
        if(data.NAMA_KELAS == "null"){
            holder.tvKelas.text  = "Tidak memiliki paket kelas"

        }else{
            holder.tvKelas.text  = data.NAMA_KELAS
            holder.tvDepoSisa.text = "Sisa deposit: ${data.DEPO_SISA.toString()}"
            holder.tvMasaBerlaku.text = "Expired: ${data.MASA_BERLAKU}"
        }


    }

    override fun getItemCount(): Int {
        return profils.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKelas: TextView
        var tvDepoSisa: TextView
        var tvMasaBerlaku: TextView

        init {
            tvKelas = view.findViewById(R.id.text_kelas_profil)
            tvDepoSisa = view.findViewById(R.id.text_depo_sisa)
            tvMasaBerlaku = view.findViewById(R.id.text_masa_berlaku)
        }

    }


}