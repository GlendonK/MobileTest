package com.example.team16_medassist.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.example.team16_medassist.R
import com.example.team16_medassist.database.FirebaseApp
import com.example.team16_medassist.databinding.ActivityLoginBinding
import com.example.team16_medassist.viewmodel.LoginViewModel
import com.example.team16_medassist.viewmodel.ViewModelFactory


class LoginActivity : AppCompatActivity() , AuthListener{

    // create the view-model
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory((application as FirebaseApp).userRepository,(application as FirebaseApp).caseRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding view model to the layout
        val binding: ActivityLoginBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginViewModel = loginViewModel
        loginViewModel.authListener = this

        // the login button in the xml is configured such that when the button is pressed
        // the login function in viewmodel is executed

    }

    override fun onStarted() {
        Toast.makeText(this, "welcome", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        loginViewModel.getUsers()?.observe(this, Observer { userInfo ->
            val myIntent = Intent(this, MainActivity::class.java)
            if (userInfo != null) {
                myIntent.putExtra("firstName", userInfo.getFirstName())
                myIntent.putExtra("lastName", userInfo.getLastName())
                myIntent.putExtra("image", userInfo.getImageURL().toString());
            }
            startActivity(myIntent)
        })
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, "signin:fail", Toast.LENGTH_SHORT).show()
    }


}

