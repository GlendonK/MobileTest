package com.example.team16_medassist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.team16_medassist.R
import com.example.team16_medassist.model.CaseModel
import com.example.team16_medassist.viewmodel.LoginViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ParamedicViewCaseFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var database: DatabaseReference


    companion object {
        private val ARG_CAUGHT = "viewPatientFragment_caught"

        fun newInstance(inputArgs: Bundle?): ParamedicViewCaseFragment {
            val args = inputArgs
            val fragment = ParamedicViewCaseFragment()
            fragment.arguments = args
            return fragment
        }

    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        val paraViewCase: View = inflater.inflate(R.layout.fragment_viewpara_case, container, false)


        val bundle = this.arguments

        val caseId = bundle!!.getString("caseID")
        val patientName = bundle!!.getString("patientName")
        val caseAddress = bundle!!.getString("address")
        val gender = bundle!!.getString("patientGender")
        val caseDate = bundle!!.getString("incidentDate")
        val caseTime = bundle!!.getString("incidentTime")
        val patientCondition = bundle!!.getString("patientCondition")
        val caseRemarks = bundle!!.getString("caseRemarks")

        paraViewCase.findViewById<TextView>(R.id.viewPFTextViewCaseNumber).text = caseId
        paraViewCase.findViewById<TextView>(R.id.editPFTextViewName).text = patientName
        paraViewCase.findViewById<TextView>(R.id.viewPFTextViewFacility).text = caseAddress
        paraViewCase.findViewById<TextView>(R.id.viewPFTextViewIncidentDate).text = caseDate
        paraViewCase.findViewById<TextView>(R.id.viewPFTextViewIncidentTime).text = caseTime
        paraViewCase.findViewById<TextView>(R.id.viewPFTextViewCondition).text = patientCondition
        paraViewCase.findViewById<TextView>(R.id.editPFTextViewGender).text = gender
        paraViewCase.findViewById<TextView>(R.id.viewPFTextViewRemarks).text = caseRemarks



        // close case
        paraViewCase.findViewById<Button>(R.id.closeIssueButton).setOnClickListener(){
            closeCase(caseAddress,caseDate,patientCondition,caseId,caseRemarks,caseTime,gender,patientName)
        }
        paraViewCase.findViewById<Button>(R.id.editCase).setOnClickListener(){
            editCase(bundle)
        }

        paraViewCase.findViewById<Button>(R.id.startDiagonsisButton).setOnClickListener(){
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.contentFrame, ParamedicVoiceFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }


        return paraViewCase


    }
    private fun closeCase(facility:String?, incidentDate: String?, condition: String?, id: String?,
                              caseRemarks: String?, incidentTime: String?,
                              gender: String?, patientName:String?){
        val updateStatus: String? = "Closed"
        val case = CaseModel(facility,incidentDate,condition,id,caseRemarks,
            updateStatus,incidentTime,gender,patientName)
        viewModel.updateCase(case)
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrame, ReportFragment())
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        transaction.commit()
    }

    private fun editCase(bundle: Bundle){
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrame, ParamedicEditCaseFragment.newInstance(bundle))
        transaction.commit()
    }
}