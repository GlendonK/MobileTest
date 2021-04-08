package com.example.team16_medassist.activity


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class SpeechToText(context: Context, textDisplay: TextView) {
    private val context = context
    private var textDisplay = textDisplay
    private var TAG = "SpeechToText"
    private val RECORD_REQUEST_CODE = 101
    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private var speechRes: String?= null
    private var speechText: String = ""

    /**
     * flags to determine if user is talking or device is listening.
     */
    var isTalking = false
    var listening = false

    /**
     * To start the speech to text feature.
     * The speech to text will keep calling itself when it ends or encounter error,
     * this is to keep the device listening and not ending the speech to text prematurely.
     * The speech to text only stops when stopSpeechToText() is called.
     * @return a string of the spoken speech.
     */
    fun startSpeechToText(): String? {
        // Intent to to launch speech recognition with desired settings.
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
        )
        // set language to default of local (english)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
                Toast.makeText(context.applicationContext, "Recording...", Toast.LENGTH_SHORT)
                    .show()
                isTalking = true

            }

            override fun onBeginningOfSpeech() {
                Log.d("SpeechToText", "onBeginningOfSpeech")

            }

            /**
             * to detect changes in speech.
             */
            override fun onRmsChanged(p0: Float) {
                /**
                 * STT ends after 5 seconds of silence by default.
                 * but it is set to end on button tap
                 */
                Log.d("SpeechToText", "onRmsChanged")

                // user tap button to make isTalking false
                if (!isTalking && listening) {
                    speechRecognizer.stopListening()
                    listening = false
                    isTalking = false
                }

                /**
                 * listening default false, onError set it to false also
                 */
                if (!listening) {
                    listening = true
                }
            }

            override fun onBufferReceived(p0: ByteArray?) {}

            override fun onEndOfSpeech() {
                Toast.makeText(context.applicationContext, "Recording Ended", Toast.LENGTH_SHORT)
                    .show()
                Log.d("SpeechToText", "onEndOfSpeech")

            }

            /**
             * when error occur, start the speech to text again.
             */
            override fun onError(p0: Int) {
                Log.d("SpeechToText", "onError")
                listening = false
                if (!listening && isTalking){
                    speechRecognizer.startListening(speechRecognizerIntent)
                }

            }

            /**
             * when results output, set the text view with the results
             */
            override fun onResults(p0: Bundle?) {
                Log.d("SpeechToText", "onResults")

                // set text of speech
                val res = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (res != null) {
                    speechRes = res[0]
                    textDisplay.text = speechRes
                    /**
                     * appending the string even on each results
                     */
                    speechText += speechRes

                }
                /**
                 * Keep the STT running by calling it again. (5sec of silence will stop it)
                 */
                speechRecognizer.startListening(speechRecognizerIntent)
                Log.d("SpeechToText", "startListening")
            }

            override fun onPartialResults(p0: Bundle?) {
                Log.d("SpeechToText", "onPartialResults")

            }

            override fun onEvent(p0: Int, p1: Bundle?) {
                Log.d("SpeechToText", "onEvent")

            }

        })
        /**
         * start listening for speech
         */
        speechRecognizer.startListening(speechRecognizerIntent)
        return speechRes

    }

    /**
     * check for microphone permission. if no permission,
     * prompt user to allow.
     */
    fun checkAudioPermission() {
        val permission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission to record denied")
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_REQUEST_CODE
            )
        }
    }

    /**
     * function to return the text of the speech
     * @return a string of the appended results of the speech
     */
    fun getSpeechText(): String {
        return this.speechText
    }

    /**
     * function to clear the
     */
    fun clearSpeechText() {
        this.speechText = ""
    }

    /**
     * to stop the speech to text.
     */
    fun stopSpeechToText() {
        this.isTalking = false
        speechRecognizer.destroy()
        Log.d(TAG, "stopSpeechToText")
    }


}
