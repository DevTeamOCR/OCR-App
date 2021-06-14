package com.example.ocrapp.viewmodel

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.ocrapp.R
import com.example.ocrapp.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel: ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()


    fun goToForgotPassword(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_loginFragment_to_recoverPasswordFragment)
    }

    fun goToRegister(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun login(email: String, pass: String, view: View, fragment: Fragment) {

        Snackbar.make(fragment.requireView(), "Iniciando sesión...", Snackbar.LENGTH_SHORT).show()
        if (!email.isEmpty() && !pass.isEmpty()) {

            auth!!.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener { authResult: AuthResult? ->

                    val id = auth!!.currentUser!!.uid
                    firestore.collection("users").document(id).get()
                        .addOnSuccessListener { user: DocumentSnapshot ->

                            val myUser = user.toObject(User::class.java)

                            if (myUser!!.type.equals("customer", ignoreCase = true)) {

                                Navigation.findNavController(view)
                                    .navigate(R.id.action_loginFragment_to_customerActivity)

                            } else {

                                Navigation.findNavController(view)
                                    .navigate(R.id.action_loginFragment_to_enterpriseActivity)

                            }
                            fragment.requireActivity().finishAffinity()


                        }


                }.addOnFailureListener { auth: Exception ->
                Snackbar.make(view, auth.localizedMessage, Snackbar.LENGTH_SHORT).show()
            }

        } else {
            val typeData = "Ingrese los datos de su cuenta"
            Snackbar.make(view, typeData, Snackbar.LENGTH_SHORT).show()
        }


    }


}