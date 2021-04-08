package com.example.team16_medassist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.team16_medassist.R
import com.example.team16_medassist.viewmodel.LoginViewModel

class ParamedicViewMainFragment: Fragment() {

    private lateinit var viewModel: LoginViewModel

    companion object {
        private val ARG_CAUGHT = "viewPatientMainFragment_caught"

        fun newInstance(inputArgs: Bundle?): ParamedicViewMainFragment {
            val args = inputArgs
            val fragment = ParamedicViewMainFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        val paraViewMain: View = inflater.inflate(R.layout.fragment_viewmain, container, false)


        val bundle = this.arguments

        //val caseId = bundle!!.getString("caseID")
        val patientName = bundle!!.getString("patientName")
        val caseAddress = bundle!!.getString("address")
        val gender = bundle!!.getString("patientGender")
        val caseDate = bundle!!.getString("incidentDate")
        val caseTime = bundle!!.getString("incidentTime")
        val patientCondition = bundle!!.getString("caseDescription")
        val caseRemarks = bundle!!.getString("caseRemarks")

        paraViewMain.findViewById<TextView>(R.id.textViewNameHolder).text = patientName
        paraViewMain.findViewById<TextView>(R.id.viewPFTextViewMainLocation).text = caseAddress
        paraViewMain.findViewById<TextView>(R.id.viewPFTextViewMainDate).text = caseDate
        paraViewMain.findViewById<TextView>(R.id.viewPFTextViewMainTime).text = caseTime
        paraViewMain.findViewById<TextView>(R.id.viewPFTextViewMainDiagnosis).text = patientCondition
        paraViewMain.findViewById<TextView>(R.id.viewPFTextViewMainGender).text = gender

        return paraViewMain

    }
}