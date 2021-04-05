package com.example.team16_medassist.model

import kotlin.properties.Delegates

class CaseDetails {

    var caseId: String? = null
    var  latitude by Delegates.notNull<Double>()
    var  longitude by Delegates.notNull<Double>()
    var  diagnosis: String? = null
    var  symptoms: String? = null
    var  time: String? = null
    var  date: String? = null
    var  gender: String? = null


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