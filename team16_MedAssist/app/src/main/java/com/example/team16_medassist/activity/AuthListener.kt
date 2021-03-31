package com.example.team16_medassist.activity

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}