//package com.example.team16_medassist.fragment
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.example.team16_medassist.R
//import com.example.team16_medassist.model.CaseModel
//import com.example.team16_medassist.viewmodel.LoginViewModel
//import com.google.firebase.database.FirebaseDatabase
//import org.w3c.dom.Text
//
//class ParamedicCaseDetailFragment : Fragment() {
//
//    private lateinit var viewModel: LoginViewModel
//
//    companion object {
//        private val ARG_CAUGHT = "CaseDetailFragment_caught"
//
//        fun newInstance(inputArgs: Bundle?): ParamedicCaseDetailFragment {
//            val args = inputArgs
//            val fragment = ParamedicCaseDetailFragment()
//            fragment.arguments = args
//            return fragment
//        }
//
//    }
//
//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?): View? {
//
//        Log.d("ARG_CAUGHT","ENTERED DOCTORs")
//        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
//        val paraCaseView: View = inflater.inflate(R.layout.fragment_paramedic_case_detail, container, false)
//
//
//        val bundle = this.arguments
//
//        // for get query
//        val caseId = bundle!!.getString("caseID")
//
//        //Display Declaration
//        //val caseDetail = findViewById<TextView>(R.id.caseDetailNumber)
//        val patientName = paraCaseView.findViewById<EditText>(R.id.editTextPatientName)
//        val facility = paraCaseView.findViewById<EditText>(R.id.editTextFacility)
//        val incidentTime = paraCaseView.findViewById<EditText>(R.id.editTextIncidentTime)
//        val incidentDate = paraCaseView.findViewById<EditText>(R.id.editTextIncidentDate)
//        val gender = paraCaseView.findViewById<EditText>(R.id.editTextGender)
//        val conditionDetail = paraCaseView.findViewById<TextView>(R.id.textViewConditionDetail)
//        val remarksDetails = paraCaseView.findViewById<TextView>(R.id.textViewRemarksDetails)
//
//        //Buttons Declaration
//        //Start and end button requires the code from glendon
//        val startButton = paraCaseView.findViewById<Button>(R.id.startDiagonsisButton)
//
//        //addAttachment and view recording need to re-consult
//        val closeIssueButton = paraCaseView.findViewById<Button>(R.id.closeIssueButton)
//
//        // Set Case ID on fragment create
//        paraCaseView.findViewById<TextView>(R.id.caseDetailNumber).setText(caseId)
//
//        closeIssueButton.setOnClickListener {
//            val mPatientName = patientName.text.toString()
//            val mFacility = facility.text.toString()
//            //val formatterTime = SimpleDateFormat("HH:mm:ss")
//            val mIncidentTime = incidentTime.text.toString()
//            //val formatter = SimpleDateFormat("dd-MM-yyyy")
//            val mIncidentDate = incidentDate.text.toString()
//            val mGender = gender.text.toString()
//            val mConditionDetail = conditionDetail.text.toString()
//
//            saveCaseIssue(mFacility,mIncidentDate, mConditionDetail, caseId.toString(),
//                mIncidentTime,mPatientName)
//            // TODO set the next page to go after closing issues
//        }
//
//        startButton.setOnClickListener {
//            val transaction = activity!!.supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.contentFrame, ParamedicVoiceFragment())
//            transaction.disallowAddToBackStack()
//            transaction.commit()
//        }
//        return paraCaseView
//
//
//    }
//    private fun saveCaseIssue(facility:String, incidentDate: String, condition: String, id: String,
//                              incidentTime: String, patientName:String){
//        val updateStatus: String? = "Closed"
//        val case = CaseModel(facility,incidentDate,condition,id,updateStatus,incidentTime,patientName)
//        viewModel.updateCase(case)
//    }
//}