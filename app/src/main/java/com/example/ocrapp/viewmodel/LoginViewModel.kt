package com.example.ocrapp.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.ocrapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel: ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()


    fun goToForgotPassword(view: View){
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_recoverPasswordFragment)
    }

    fun goToRegister(view: View){
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun login(email: String, pass: String, view: View){
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_customerActivity)

        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_enterpriseActivity)
    }

}