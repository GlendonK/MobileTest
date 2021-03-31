package com.example.team16_medassist.model

class UserModel {
    private var firstName: String? = null
    private var lastName: String? = null
    private var onDuty: Boolean? = null
    private var position: String? = null
    var image: Any? = null

    // Default constructor required for calls to
    // DataSnapshot.getValue(FriendlyMessage.class)
    constructor() {}
    constructor(firstName: String?, image: Any, lastName: String?, onDuty: Boolean?, position: String?) {
        this.firstName = firstName
        this.lastName = lastName
        this.image = image
        this.onDuty = onDuty
        this.position = position
    }


    fun getFirstName(): String? {
        return firstName
    }

    fun getLastName(): String? {
        return lastName
    }

    fun getImageURL(): Any? {
        return image
    }

    fun getDutyStatus(): Boolean? {
        return onDuty
    }

    fun getPosition(): String? {
        return position
    }



}