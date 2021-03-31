package com.example.team16_medassist.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.team16_medassist.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ParaVoiceActivity : AppCompatActivity() {

    val TAG: String = "ParaVoiceActivity"
    private lateinit var mSymptomsText: TextView
    private lateinit var speechRecognizer: SpeechRecognizer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paramedic_voice_record_activity)

        val msStartRecButton: Button = findViewById(R.id.startRecButton)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)


        mSymptomsText = findViewById(R.id.symptomsText)
        val mDiagnosisText: TextView = findViewById(R.id.diagnosisText)
        val mSubmitButton: Button = findViewById(R.id.submitButton)

        // SpeechToText class takes in a current Context and TextView(to display) somehow the sequnces gets messed up if i don setText in the STT class
        val speechToText = SpeechToText(this, mSymptomsText)
        val predictDiagnosis = PredictDiagnosis(this)

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

                Toast.makeText(applicationContext, "$symptoms", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "$symptoms")

                // Initialise the PredictDiagnosis class @Params(Context)
                // Classify and get a diagnosis
                var res = predictDiagnosis.getDiagnosis(symptoms)
                mDiagnosisText.text = res

                // Clear speechText so that it does not keep appending
                speechToText.clearSpeechText()
            }
            Toast.makeText(this, "STOPPING", Toast.LENGTH_SHORT).show()

        }
    }
}