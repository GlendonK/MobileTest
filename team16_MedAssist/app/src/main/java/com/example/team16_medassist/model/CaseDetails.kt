package com.example.team16_medassist.model

import kotlin.properties.Delegates

class CaseDetails {

    var caseId: String? = null
    private var  latitude by Delegates.notNull<Double>()
    private var  longitude by Delegates.notNull<Double>()
    private var  diagnosis: String? = null
    private var  symptoms: String? = null
    private var  time: String? = null
    private var  date: String? = null
    private var  gender: String? = null


    constructor() {}

    constructor( caseId: String,  latitude: Double,  longitude: Double,  diagnosis: String,  symptoms: String,  time: String,  date: String,  gender: String,
    ) {
        this.caseId = caseId
        this.latitude = latitude
        this.longitude = longitude
        this.diagnosis = diagnosis
        this.symptoms = symptoms
        this.time = time
        this.date = date
        this.gender = gender
    }

}