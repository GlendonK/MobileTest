package com.example.team16_medassist.activity

import android.content.Context
import android.widget.Toast
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.lang.Exception
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


class PredictDiagnosis(context: Context) {

    private val context = context
    private val TAG = "PredictDiagnosis"

    /**
     * the symptom keywords for the ML model to recognise and predict a diagnosis
     */
    private val symptomsKeywords: String =
        "bleeding,chest,pain,dizziness,cold,pale,vomiting,nausea,abdominal,fever,shortness,breath,sweating,unconscious,fatigue,obese,cough,wheezing,numbness,confusion,headache,bruising,swelling,arm,leg,tenderness,sneezing,rash,itchy,blisters,skin,diarrhea,tightness,cramps,rapid,heart,seizures,slow,throat"

    /**
     * function to encode the keyword from string as binary form (1s and 0s)
     * 1 represent the keyword is present, 0 represent the keyword is not present.
     * returns a float as the model takes in as float.
     * @param symptomsKeywords - the string of symptom keywords
     * @param speechToText - the text results from the speech to text
     * @return FloatArray
     */
    private fun extractKeywordAsBinary(symptomsKeywords: String, speechToText: String): FloatArray {
        val symptomsKeywordsSplit: List<String> = symptomsKeywords.split(",")
        val speechToTextSplit: List<String> = speechToText.split(" ")
        var resArr: FloatArray = floatArrayOf()
        symptomsKeywordsSplit.forEach {
            var cur = it
            if (speechToTextSplit.contains(cur)) {
                resArr += 1f
            } else {
                resArr += 0f
            }
        }
        return resArr
    }

    /**
     * open the tflite model.
     */
    fun loadModelFile(): MappedByteBuffer? {
        val assetFileDescriptor = context.applicationContext.assets.openFd("diagnosis_model.tflite")
        val fileInputStream = FileInputStream(assetFileDescriptor.getFileDescriptor())
        val fileChannel = fileInputStream.channel
        val startOffSet = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSet, declaredLength)
    }

    /**
     * classification of the diagnosis based on the encoded symptom keywords.
     * @param speechToText
     * @return FloatArray
     */
    fun classify(speechToText: String): FloatArray {
        val interpreter = Interpreter(loadModelFile()!!)
        /*val inputs : Array<FloatArray> = arrayOf(floatArrayOf(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f,
            1f, 1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f))*/
        val inputs = extractKeywordAsBinary(this.symptomsKeywords, speechToText)
        val outputs: Array<FloatArray> = arrayOf(
            floatArrayOf(
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                0f
            )
        )

        try {
            interpreter.run(inputs, outputs)
        } catch (e: Exception) {
            Toast.makeText(this.context.applicationContext, "Error in ML", Toast.LENGTH_SHORT)
                .show()
        }
        return outputs[0]
    }

    /**
     * to perform the the classification to get the most probable diagnosis.
     * the model will output the probabilities of all the 23 classes (diagnosis) as a float array.
     * the index of the highest number/probability in the float array is the most probable diagnosis.
     * @param speechToText - the text result from the speech to text
     * @return - String after checking the diagnosis index
     */
    fun getDiagnosis(speechToText: String): String? {
        val modelOutput = classify(speechToText)
        var outputArr: FloatArray = floatArrayOf()

        modelOutput.forEach { i -> outputArr += i }

        val mostProbableRes = outputArr.maxOf { it }
        var indexOfMostProbable = 0
        var i = 0
        outputArr.forEach {
            if (it != mostProbableRes) {
                i++
            } else if (it == mostProbableRes) {
                indexOfMostProbable = i
            }
        }

        return checkDiagnosis(indexOfMostProbable)
    }

    /**
     * to check which diagnosis the index of the ML model float array output  belongs to.
     * @param index - the index of the highest probability from getDiagnosis function.
     * @return String diagnosis text representing the index.
     */

    private fun checkDiagnosis(index: Int): String? {
        var diagnosis: String? = null
        when (index) {
            0 -> diagnosis = "HYPOGLYCEMIA"
            1 -> diagnosis = "PNEUMONIA"
            2 -> diagnosis = "TUBERCULOSIS"
            3 -> diagnosis = "ANEMIA"
            4 -> diagnosis = "ASPHYXIA"
            5 -> diagnosis = "ASTHMA"
            6 -> diagnosis = "BROKEN BONE"
            7 -> diagnosis = "CARDIAC ARREST"
            8 -> diagnosis = "CONCUSSION"
            9 -> diagnosis = "COVID"
            10 -> diagnosis = "DEEP LACERATION"
            11 -> diagnosis = "DENGUE FEVER"
            12 -> diagnosis = "DRUG REACTION"
            13 -> diagnosis = "EPILEPSY"
            14 -> diagnosis = "FLU"
            15 -> diagnosis = "FOOD POISONING"
            16 -> diagnosis = "HEART ATTACK"
            17 -> diagnosis = "HEAT STROKE"
            18 -> diagnosis = "INTERNAL BLEEDING"
            19 -> diagnosis = "PNEUMOTHORAX"
            20 -> diagnosis = "SEVERE ALLERGY"
            21 -> diagnosis = "SEVERE BURNS"
            22 -> diagnosis = "STROKE"
        }
        return diagnosis
    }


}