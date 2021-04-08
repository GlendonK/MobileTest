package com.example.team16_medassist.fragment

import HistoryFragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.team16_medassist.R
import com.example.team16_medassist.adaptor.DoctorRecentCasesRecyclerAdaptor
import com.example.team16_medassist.adaptor.RecentCasesRecyclerAdaptor
import com.example.team16_medassist.viewmodel.LoginViewModel


class DoctorFragment : Fragment(){

    private lateinit var viewModel : LoginViewModel



    companion object {
        private val ARG_CAUGHT = "homeFragment_caught"

        fun newInstance(inputArgs: Bundle?): DoctorFragment {
            val args = inputArgs
            val fragment = DoctorFragment()
            fragment.arguments = args
            return fragment
        }

    }


    @NonNull
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {


        val doctorView : View = inflater.inflate(R.layout.fragment_doctor,container,false)

        // declare recyclerView
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        val doctorRecyclerView = doctorView.findViewById<RecyclerView>(R.id.doctorRecyclerView)

        viewModel.getCaseLiveData().observe(viewLifecycleOwner, Observer { cases ->
            // update the recyclerview
            val recyclerViewAdapter = DoctorRecentCasesRecyclerAdaptor(cases, doctorView.context)
            doctorRecyclerView.layoutManager = LinearLayoutManager(activity)
            doctorRecyclerView.adapter = recyclerViewAdapter
            recyclerViewAdapter.notifyDataSetChanged()
        })


        return doctorView


    }





}