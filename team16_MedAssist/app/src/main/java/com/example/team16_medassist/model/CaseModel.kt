package com.example.team16_medassist.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Time
import java.util.*

class CaseModel {
    private var caseAddress: String? = null
    private var caseDate: String? = null
    private var caseDescription: String? = null
    private var caseId: String? = null
    private var caseRemarks: String? = null
    private var caseStatus: String? = null
    private var caseTime: String? = null
    private var gender: String? = null
    private var patientName : String? = null

    // Default constructor required for calls to
    // DataSnapshot.getValue(CaseModel.class)
    constructor() {}
    constructor(caseAddress: String?, caseDate: String?, caseDescription: String?,
                caseId: String?, caseRemarks: String?, caseStatus: String?, caseTime: String?,
                gender:String? ,patientName:String?) {
        this.caseAddress = caseAddress
        this.caseDate = caseDate
        this.caseDescription = caseDescription
        this.caseId = caseId
        this.caseRemarks = caseRemarks
        this.caseStatus = caseStatus
        this.caseTime = caseTime
        this.gender = gender
        this.patientName = patientName
    }

    fun getCaseAddress(): String? {
        return caseAddress
    }

    fun getCaseDate(): String? {
        return caseDate
    }

    fun getCaseDescription(): String? {
        return caseDescription
    }

    fun getCaseId(): String? {
        return caseId
    }

    fun getCaseStatus(): String? {
        return caseStatus
    }

    fun getCaseTime(): String? {
        return caseTime
    }

    fun getPatientName(): String? {
        return patientName
    }

    fun getGender(): String?{
        return gender
    }

    fun getCaseRemarks(): String? {
        return caseRemarks
    }


}
