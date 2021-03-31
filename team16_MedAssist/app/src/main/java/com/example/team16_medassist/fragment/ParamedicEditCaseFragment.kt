package com.example.team16_medassist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.team16_medassist.R
import com.example.team16_medassist.model.CaseModel
import com.example.team16_medassist.viewmodel.LoginViewModel

class ParamedicEditCaseFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    companion object {
        private val ARG_CAUGHT = "editPatientFragment_caught"

        fun newInstance(inputArgs: Bundle?): ParamedicEditCaseFragment {
            val args = inputArgs
            val fragment = ParamedicEditCaseFragment()
            fragment.arguments = args
            return fragment
        }

    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        val paraEditCase: View = inflater.inflate(R.layout.fragment_editpara_case, container, false)


        val bundle = this.arguments

        val caseId = bundle!!.getString("caseID")
        val patientName = bundle!!.getString("patientName")
        val caseAddress = bundle!!.getString("address")
        val gender = bundle!!.getString("patientGender")
        val caseDate = bundle!!.getString("incidentDate")
        val caseTime = bundle!!.getString("incidentTime")
        val patientCondition = bundle!!.getString("patientCondition")
        val caseRemarks = bundle!!.getString("caseRemarks")


        paraEditCase.findViewById<TextView>(R.id.viewPFTextViewCaseNumber).text = caseId
        paraEditCase.findViewById<EditText>(R.id.editPFTextViewName).setText(patientName.toString())
        paraEditCase.findViewById<EditText>(R.id.editPFTextViewFacility).setText(caseAddress)
//        paraEditCase.findViewById<EditText>(R.id.editPFTextViewIncidentDate).setText(caseDate)
//        paraEditCase.findViewById<EditText>(R.id.editPFTextViewIncidentTime).setText(caseTime)
        paraEditCase.findViewById<EditText>(R.id.editPFTextViewCondition).setText(patientCondition)
//        paraEditCase.findViewById<EditText>(R.id.editPFTextViewGender).setText(gender)
        paraEditCase.findViewById<EditText>(R.id.editPFTextViewRemarks).setText(caseRemarks)

        return paraEditCase


    }
}