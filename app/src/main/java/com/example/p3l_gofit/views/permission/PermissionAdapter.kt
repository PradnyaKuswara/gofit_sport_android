package com.example.p3l_gofit.views.permission

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_gofit.Dokter
import com.example.p3l_gofit.R
import com.example.p3l_gofit.models.InstructorPermission
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionAdapter(private var permissions: List<InstructorPermission>, context: Context): RecyclerView.Adapter<PermissionAdapter.ViewHolder>()
{
    private val context: Context

    init {
        this.context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_item_permission, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PermissionAdapter.ViewHolder, position: Int) {
        val data = permissions[position]
        holder.tvKeterangan.text = data.KETERANGAN_IZIN
        holder.tvTanggalIzin.text = "Tanggal Izin: ${data.TANGGAL_IZIN_INSTRUKTUR}"
        holder.tvMelakukan.text = "Tanggal Melakukan Izin: ${data.TANGGAL_MELAKUKAN_IZIN}"
        holder.tvStatus.text = "${data.STATUS_IZIN} - ${data.TANGGAL_KONFIRMASI_IZIN}"
        if(holder.tvStatus.text == "null - null") {
            holder.tvStatus.text = "Belum dikonfirmasi"
        }
        holder.cvpermission.setOnClickListener{
            Toast.makeText(context,data.KETERANGAN_IZIN,Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return permissions.size
    }

//    fun setPermissionList(permissionss: Array<InstructorPermission>){
//        this.permissions = permissionss.toList()
//    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKeterangan: TextView
        var tvTanggalIzin: TextView
        var tvMelakukan: TextView
        var tvStatus: TextView
        var cvpermission: CardView

        init {
           tvKeterangan = view.findViewById(R.id.text_keterangan)
            tvTanggalIzin = view.findViewById(R.id.text_tanggal_izin)
            tvMelakukan = view.findViewById(R.id.text_melakukan_tanggal_izin)
            tvStatus = view.findViewById(R.id.text_status)
            cvpermission = view.findViewById(R.id.cv_permission)
        }
    }
}