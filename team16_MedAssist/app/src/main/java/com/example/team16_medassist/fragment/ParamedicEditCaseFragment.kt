package com.example.team16_medassist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.team16_medassist.R
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
        val paraEditCaseView: View = inflater.inflate(R.layout.fragment_editpara_case, container, false)


        val bundle = this.arguments

        val caseId = bundle!!.getString("caseID")
        val patientName = bundle!!.getString("patientName")
        val caseAddress = bundle!!.getString("address")
        val gender = bundle!!.getString("patientGender").toString()
        val caseDate = bundle!!.getString("incidentDate")
        val caseTime = bundle!!.getString("incidentTime")
        val patientCondition = bundle!!.getString("caseDescription")
        val caseRemarks = bundle!!.getString("caseRemarks")

        // declare all text fields ids
        val mCaseNumber =  paraEditCaseView.findViewById<TextView>(R.id.viewPFTextViewCaseNumber)
        val mPatientName =   paraEditCaseView.findViewById<EditText>(R.id.editPFTextViewName)
        val mPatientAddress = paraEditCaseView.findViewById<EditText>(R.id.editPFTextViewFacility)
        val mCaseDescription = paraEditCaseView.findViewById<EditText>(R.id.editPFTextViewCondition)
        val mCaseRemarks = paraEditCaseView.findViewById<EditText>(R.id.editPFTextViewRemarks)
        val mIncidentDate = paraEditCaseView.findViewById<EditText>(R.id.editPFTextViewIncidentDate)
        val mIncidentTime = paraEditCaseView.findViewById<EditText>(R.id.editPFTextViewIncidentTime)

        // declare spinner view
        val mGenderSpinner = paraEditCaseView.findViewById<Spinner>(R.id.spinnerGender)


        mCaseNumber.text = caseId
        mPatientName.setText(patientName.toString())
        mPatientAddress.setText(caseAddress)
        mIncidentDate.setText(caseDate)
        mIncidentTime.setText(caseTime)
        mCaseDescription.setText(patientCondition)
        mCaseRemarks.setText(caseRemarks)

        val genderArr = resources.getStringArray(R.array.Gender)
        //access the spinner
        if (mGenderSpinner != null) {
            val adapter = ArrayAdapter(paraEditCaseView.context,
                    android.R.layout.simple_spinner_item, genderArr)
            mGenderSpinner.adapter = adapter
            val position = 0
            if (gender.equals('M')){
                mGenderSpinner.setSelection(position)
            }else{
                mGenderSpinner.setSelection(position)
            }
            mGenderSpinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    mGenderSpinner.setSelection(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        paraEditCaseView.findViewById<Button>(R.id.updateCaseButton).setOnClickListener(){
            bundle.putString("patientName",  mPatientName.text.toString())
            bundle.putString("address",  mPatientAddress.text.toString())
            bundle.putString("caseDescription",  mCaseDescription.text.toString())
            bundle.putString("caseRemarks",  mCaseRemarks.text.toString())
            bundle.putString("incidentDate",  mIncidentDate.text.toString())
            bundle.putString("incidentTime",  mIncidentTime.text.toString())
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.contentFrame, ParamedicViewCaseFragment.newInstance(bundle))
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return paraEditCaseView


    }


}