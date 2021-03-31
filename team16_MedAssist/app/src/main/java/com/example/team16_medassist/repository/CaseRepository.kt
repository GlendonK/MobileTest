package com.example.team16_medassist.repository

import androidx.lifecycle.MutableLiveData
import com.example.team16_medassist.database.CaseDatabase
import com.example.team16_medassist.model.CaseModel


class CaseRepository(private val caseDatabase: CaseDatabase) {

    fun queryCase(uid: String) = caseDatabase.queryCase(uid)

    fun getCase() = caseDatabase.getCase()

    fun getCaseReportCount() = caseDatabase.getCaseReportCount()

    fun updateCase(caseModel: CaseModel?) = caseDatabase.updateQuery(caseModel)

    val  mCaseData = caseDatabase.caseArray
}