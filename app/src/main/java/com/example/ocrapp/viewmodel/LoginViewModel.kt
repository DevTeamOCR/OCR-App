package com.example.ocrapp.viewmodel

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.ocrapp.R
import com.example.ocrapp.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class LoginViewModel : ViewModel() {



    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()


    fun goToForgotPassword(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_loginFragment_to_recoverPasswordFragment)
    }

    fun goToRegister(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun login(email: String, pass: String, view: View, fragment: Fragment) {

        Snackbar.make(fragment.requireView(), "Iniciando sesión...", Snackbar.LENGTH_SHORT).show()

        if (email.isNotEmpty() && pass.isNotEmpty()) {

            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {

                    val id = auth.currentUser?.uid
                    if (id != null) {
                        firestore.collection("users").document(id).get()
                            .addOnSuccessListener { user: DocumentSnapshot ->

                                val myUser = user.toObject<User>()

                                Log.d("User:", "$myUser")
                                if (myUser?.type.equals("customer")) {

                                    Navigation.findNavController(view)
                                        .navigate(R.id.action_loginFragment_to_customerActivity)

                                } else {

                                    Navigation.findNavController(view)
                                        .navigate(R.id.action_loginFragment_to_enterpriseActivity)

                                }
                                fragment.requireActivity().finishAffinity()
                            }
                    }


                }.addOnFailureListener { fail ->
                    Snackbar.make(view, fail.message.toString(), Snackbar.LENGTH_SHORT).show()
                }

        } else {
            val typeData = "Ingrese los datos de su cuenta"
            Snackbar.make(view, typeData, Snackbar.LENGTH_SHORT).show()
        }


    }

}