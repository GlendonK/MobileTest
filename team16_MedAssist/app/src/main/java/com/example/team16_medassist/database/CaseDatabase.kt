package com.example.team16_medassist.database

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.team16_medassist.model.CaseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CaseDatabase {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private var caseModel =  MutableLiveData<CaseModel?>()
    private var numTotalReport= MutableLiveData<Long?>()
    var caseArray= MutableLiveData<ArrayList<CaseModel>>()
    var tempArr = ArrayList<CaseModel>()

    fun queryCase(userId : String){
        val query = Firebase.database.getReference("case")
        query.child(userId).addValueEventListener(object : ValueEventListener {
            // defined by valueeventlistener if the read is cancelled
            override fun onCancelled(error: DatabaseError) {
                Log.e("onCancelledError", "onCancelled", error.toException())
            }
            // use dataonchange to read static snapshot of the content at the given path
            // this is triggered when the listener attaches and when the data/child changes
            override fun onDataChange(dataSnapShot: DataSnapshot) {
                if (dataSnapShot.exists()) {
                    numTotalReport!!.postValue(dataSnapShot.childrenCount)
                    // retrieve the latest case
                    caseModel!!.postValue(dataSnapShot.children.first().getValue(CaseModel::class.java))
                    // retrieve an array of case objects
                    tempArr.clear()
                    for(ds in dataSnapShot.children) {
                        //Log.d("test",ds.child("caseId").getValue(String::class.java).toString())
                        tempArr.add(ds.getValue(CaseModel::class.java)!!)
                        caseArray.postValue(tempArr)
                    }
                }
            }
        })
    }

    fun updateQuery(caseModel: CaseModel?){
        val query = Firebase.database.getReference("case").child(currentUser().uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val childUpdates = HashMap<String, Any>()
                for (i in tempArr.indices) {
                    if (tempArr[i].getCaseId().toString() == caseModel!!.getCaseId().toString()) {
                        val childPath = (i + 1).toString()
                        childUpdates.put("caseAddress",caseModel.getCaseAddress().toString())
                        childUpdates.put("caseStatus",caseModel.getCaseStatus().toString())
                        query.child(childPath).updateChildren(childUpdates)
                        break
                    }
                }
            }
        })

    }


    fun getCase(): MutableLiveData<CaseModel?> {
        return (this.caseModel)
    }

    // outstanding reports
    fun getCaseReportCount(): MutableLiveData<Long?> {
        return (this.numTotalReport)
    }

    fun currentUser() = firebaseAuth.currentUser!!


}