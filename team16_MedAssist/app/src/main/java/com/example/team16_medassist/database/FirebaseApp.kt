package com.example.team16_medassist.database

import android.app.Application
import com.example.team16_medassist.repository.CaseRepository
import com.example.team16_medassist.repository.UserRepository


class FirebaseApp: Application() {
    private val userfirebase by lazy {UserDatabase()}
    private val casefirebase by lazy {CaseDatabase()}
    val userRepository by lazy{ (UserRepository(userfirebase))}
    val caseRepository by lazy{ (CaseRepository(casefirebase))}
}