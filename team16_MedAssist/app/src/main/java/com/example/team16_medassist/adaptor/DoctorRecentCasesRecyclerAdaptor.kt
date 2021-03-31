package com.example.team16_medassist.adaptor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.team16_medassist.R
import com.example.team16_medassist.model.CaseModel

class DoctorRecentCasesRecyclerAdaptor (val arrayList: ArrayList<CaseModel>,
                                        val context: Context): RecyclerView.Adapter<DoctorRecentCasesRecyclerAdaptor.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(model: CaseModel){
            Log.d("CHECKS","ENTERED HERE")
            itemView.findViewById<TextView>(R.id.textViewPatientCaseID).text = model.getCaseId()
            //itemView.findViewById<ImageView>(R.id.imageViewHistoryCaseLocation).setImageResource(R.drawable.ic_baseline_location_on_24)
            itemView.findViewById<TextView>(R.id.textViewPatientCaseCondition).text = model.getCaseDescription()
            itemView.findViewById<TextView>(R.id.textViewPatientCaseDate).text = model.getCaseDate()
            itemView.findViewById<TextView>(R.id.textViewPatientCaseTime).text = model.getCaseTime()

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.doctor_case_recyclerviewmodel,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (arrayList!=null){
            holder.bindItems(arrayList[position]!!)
        }
    }
}
