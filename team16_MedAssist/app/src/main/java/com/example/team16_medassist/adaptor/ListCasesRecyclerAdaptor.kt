package com.example.team16_medassist.adaptor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.team16_medassist.R
import com.example.team16_medassist.model.CaseModel
import java.util.*


class ListCasesRecyclerAdaptor (val arrayList: ArrayList<CaseModel>,
    val context: Context,private val itemClickListener: OnItemClickListener):
        RecyclerView.Adapter<ListCasesRecyclerAdaptor.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bindItems(model: CaseModel, clickListener: OnItemClickListener){
            itemView.findViewById<TextView>(R.id.caseId).text = model.getCaseId()
            itemView.findViewById<TextView>(R.id.patientName).text = model.getPatientName()
            itemView.findViewById<TextView>(R.id.caseDescription).text = model.getCaseDescription()

            //change initials
            val patientInitials =model.getPatientName().toString().get(0)
            itemView.findViewById<TextView>(R.id.patientInitials).text = patientInitials.toString()
            val androidColors: IntArray = itemView.context.getResources().getIntArray(R.array.androidcolors)
            val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
            val background = itemView.findViewById<TextView>(R.id.patientInitials).getBackground();
            background.setTint(randomAndroidColor)

            itemView.findViewById<ImageButton>(R.id.imageButtonHistory).setOnClickListener {
                clickListener.onItemClicked(model)
                //Log.d("tag",clickListener.onItemClicked(model).toString())
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.recyclerview_pastcases,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (arrayList!=null){
            holder.bindItems(arrayList[position]!!,itemClickListener)
        }
    }
}


