package com.example.team16_medassist.repository


import com.example.team16_medassist.database.UserDatabase

// a Repository is a class that abstracts access to multiple data sources.

class UserRepository(private val userDatabase: UserDatabase){

    fun login(email: String, password: String) = userDatabase.login(email,password)

    fun currentUser() = userDatabase.currentUser()


    fun logout() = userDatabase.logout()

    fun queryUser(uid: String) = userDatabase.queryUser(uid)

    fun getUser() = userDatabase.getUser()

}