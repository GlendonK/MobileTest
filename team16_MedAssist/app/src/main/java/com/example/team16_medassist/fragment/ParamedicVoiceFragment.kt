package com.example.team16_medassist.fragment

import HistoryFragment
import android.graphics.Color
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.team16_medassist.R
import com.example.team16_medassist.activity.PredictDiagnosis
import com.example.team16_medassist.activity.SpeechToText
import com.example.team16_medassist.adaptor.DoctorRecentCasesRecyclerAdaptor
import com.example.team16_medassist.adaptor.RecentCasesRecyclerAdaptor
import com.example.team16_medassist.model.CaseDetails
import com.example.team16_medassist.viewmodel.LoginViewModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_viewpara_case.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ParamedicVoiceFragment : Fragment(){

    private lateinit var viewModel : LoginViewModel
    private lateinit var mSymptomsText: TextView
    private lateinit var speechRecognizer: SpeechRecognizer

    /**
     * Firebase
     */
    private var mFirebaseDatabase: DatabaseReference?=null
    private var mFirebaseInstance: FirebaseDatabase?=null


    companion object {
        private val ARG_CAUGHT = "homeFragment_caught"

        fun newInstance(inputArgs: Bundle?): ParamedicVoiceFragment {
            val args = inputArgs
            val fragment = ParamedicVoiceFragment()
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


        val ParaVoiceView : View = inflater.inflate(R.layout.paramedic_voice_record_activity,container,false)

        // declare recyclerView
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)

        val msStartRecButton: Button = ParaVoiceView.findViewById(R.id.startRecButton)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(ParaVoiceView.context)


        mSymptomsText = ParaVoiceView.findViewById(R.id.symptomsText)
        val mDiagnosisText: TextView = ParaVoiceView.findViewById(R.id.diagnosisText)
        val mSubmitButton: Button = ParaVoiceView.findViewById(R.id.submitButton)

        // SpeechToText class takes in a current Context and TextView(to display) somehow the sequnces gets messed up if i don setText in the STT class
        val speechToText = SpeechToText(ParaVoiceView.context, mSymptomsText)
        val predictDiagnosis = PredictDiagnosis(ParaVoiceView.context)

        msStartRecButton.setOnClickListener {
            speechToText.checkAudioPermission()
            // this is async
            speechToText.startSpeechToText()
        }
        mSubmitButton.setOnClickListener {

            lifecycleScope.launch {
                // delay to make sure the speech recognizer finish transcribing
                delay(500)
                speechToText.stopSpeechToText()

                val symptoms = speechToText.getSpeechText()




                Toast.makeText(ParaVoiceView.context, "$symptoms", Toast.LENGTH_SHORT).show()
                Log.d(ARG_CAUGHT, "$symptoms")

                // Initialise the PredictDiagnosis class @Params(Context)
                // Classify and get a diagnosis
                var res = predictDiagnosis.getDiagnosis(symptoms)
                mDiagnosisText.text = res

                // Clear speechText so that it does not keep appending
                speechToText.clearSpeechText()



                mFirebaseInstance= FirebaseDatabase.getInstance()
                mFirebaseDatabase=mFirebaseInstance!!.getReference("active_case_details")
                var store: CaseDetails? = null
                mFirebaseDatabase?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            dataSnapshot.children.forEach {
                                store = it!!.getValue(CaseDetails::class.java)
                                Log.e("ParamedicCaseDetailFragment", "${store!!.caseId}")
                                var caseId = store!!.caseId
                                var latitude = store!!.latitude
                                var longitude = store!!.longitude
                                var time = store!!.time
                                var date = store!!.date
                                var gender = store!!.gender



                                var case = CaseDetails(caseId!!, latitude,longitude,mDiagnosisText.text.toString(), symptoms, time!!, date!!, gender!!)
//
//                                mFirebaseDatabase!!.child("").setValue(mDiagnosisText.text.toString())


                                var database = Firebase.database.reference.child("ML").child("$caseId")
                                database.child("diagnosis").setValue(mDiagnosisText.text.toString())
                                database.child("symptoms").setValue(symptoms)


                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
            Toast.makeText(ParaVoiceView.context, "STOPPING", Toast.LENGTH_SHORT).show()

        }


        return ParaVoiceView
    }


}