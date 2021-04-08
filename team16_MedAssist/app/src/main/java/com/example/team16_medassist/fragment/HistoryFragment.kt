import android.os.Bundle
import android.util.Log
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
import com.example.team16_medassist.adaptor.ListCasesRecyclerAdaptor
import com.example.team16_medassist.adaptor.OnItemClickListener
import com.example.team16_medassist.fragment.HomeFragment
import com.example.team16_medassist.fragment.ParamedicViewCaseFragment
import com.example.team16_medassist.fragment.ParamedicViewMainFragment
import com.example.team16_medassist.fragment.ReportFragment
import com.example.team16_medassist.model.CaseModel
import com.example.team16_medassist.viewmodel.LoginViewModel

class HistoryFragment : Fragment() , OnItemClickListener {

    private lateinit var viewModel : LoginViewModel

    companion object {
        private val ARG_CAUGHT = "historyFragment_caught"

        fun newInstance(inputArgs: Bundle?): HomeFragment {
            val args = inputArgs
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)

        val historyView: View = inflater.inflate(R.layout.fragment_history, container, false)

        val casesListRecyclerView = historyView.findViewById<RecyclerView>(R.id.historyRecyclerView)
        // listens for changes in firebase
        viewModel.getCaseLiveData().observe(viewLifecycleOwner, Observer { cases ->

            // update the number of caseHistory
            val getCaseCount = viewModel.getCaseReportCount().value.toString()
            historyView.findViewById<TextView>(R.id.titleNumber).text = "(" + getCaseCount + ")"

            // update the number of items in the recent activity recyclerview
            val recyclerViewAdapter = ListCasesRecyclerAdaptor(cases, historyView.context,this)
            casesListRecyclerView.layoutManager = LinearLayoutManager(activity)
            casesListRecyclerView.adapter = recyclerViewAdapter
            recyclerViewAdapter.notifyDataSetChanged()
        })

        return historyView

    }
    override fun onItemClicked(caseModel: CaseModel) {
        //TODO obtained the id here : can get all the fields in the recycler widget
        Log.d("tag",caseModel.getCaseId().toString())
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
        transaction.replace(R.id.contentFrame, ParamedicViewMainFragment.newInstance(bundle))
        transaction.addToBackStack(null)
        transaction.commit()

    }


}