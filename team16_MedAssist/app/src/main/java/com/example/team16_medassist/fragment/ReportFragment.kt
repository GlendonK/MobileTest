package com.example.team16_medassist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.team16_medassist.R
import com.example.team16_medassist.adaptor.ListReportsRecyclerAdaptor
import com.example.team16_medassist.adaptor.OnItemClickListener
import com.example.team16_medassist.model.CaseModel
import com.example.team16_medassist.viewmodel.LoginViewModel

class ReportFragment: Fragment(), OnItemClickListener {
    //TODO Report Fragment

    private lateinit var viewModel : LoginViewModel

    companion object {
        private val ARG_CAUGHT = "reportFragment_caught"

        fun newInstance(inputArgs: Bundle?): ReportFragment {
            val args = inputArgs
            val fragment = ReportFragment()
            fragment.arguments = args
            return fragment
        }

    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)

        val reportView: View = inflater.inflate(R.layout.fragment_reports, container, false)


        val reportListRecyclerView = reportView.findViewById<RecyclerView>(R.id.reportsRecyclerView)
        // listens for changes in firebase
        viewModel.getCaseLiveData().observe(viewLifecycleOwner, Observer { cases ->

            var pendingReportArr = ArrayList<CaseModel>()

            // update the number of report
            val caseId = viewModel.getCases().value!!.getCaseId()
            val caseStatus = viewModel.getCases().value!!.getCaseStatus()
            for (i in cases.indices){
                if (cases[i].getCaseStatus().toString()=="Pending"){
                    //pendingReportArr.clear()
                    pendingReportArr.add(cases[i])
                }
            }
            reportView.findViewById<TextView>(R.id.reportFTextViewCount).text = "(" + pendingReportArr.size + ")"
            // update the number of items in the recent activity recyclerview
            val recyclerViewAdapter = ListReportsRecyclerAdaptor(pendingReportArr, reportView.context,this)
            reportListRecyclerView.layoutManager = LinearLayoutManager(activity)
            reportListRecyclerView.adapter = recyclerViewAdapter
            recyclerViewAdapter.notifyDataSetChanged()
        })


        return reportView

    }


    /**
     *  this function listens to the patient selected and retrieve
     *  all information from the card to display in the patient
     *  case details
     *  - use bundle to send the 'data' class
     */

    override fun onItemClicked(caseModel: CaseModel) {
        val bundle = Bundle()
        bundle.putString("caseID", caseModel.getCaseId().toString())
        bundle.putString("patientName", caseModel.getPatientName().toString())
        bundle.putString("patientGender", caseModel.getGender().toString())
        bundle.putString("address", caseModel.getCaseAddress().toString())
        bundle.putString("incidentTime", caseModel.getCaseTime().toString())
        bundle.putString("incidentDate", caseModel.getCaseDate().toString())
        bundle.putString("caseRemarks", caseModel.getCaseRemarks().toString())
        bundle.putString("caseDescription", caseModel.getCaseDescription().toString())
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrame, ParamedicViewCaseFragment.newInstance(bundle))
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
