package com.example.ocrapp.viewmodel

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.ocrapp.R
import com.example.ocrapp.model.User
import com.example.ocrapp.util.AppUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel {

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val user = MutableLiveData<User>()

    fun registerUser(password: TextInputLayout, view: View, fragment: Fragment) {

        Snackbar.make(view, "Registrando...", Snackbar.LENGTH_SHORT).show()
        val myUser = user.value
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(myUser!!.email, AppUtils.getText(password))
            .addOnSuccessListener { authResult: AuthResult? ->

                if (myUser.type == "Cliente") {
                    myUser.type = "customer"
                } else {
                    myUser.type = "enterprise"
                }

                val uid = FirebaseAuth.getInstance().uid
                myUser.uid = uid!!
                firestore.collection("users").document(uid!!).set(myUser)

                if (myUser.type.equals("customer")) {

                    Navigation.findNavController(view)
                        .navigate(R.id.action_registerFragment_to_loginFragment)

                } else {

                    Navigation.findNavController(view)
                        .navigate(R.id.action_registerFragment_to_loginFragment)

                }

                fragment.requireActivity().finishAffinity()

            }.addOnFailureListener { fail: Exception ->
            Snackbar.make(view, fail.localizedMessage, Snackbar.LENGTH_SHORT).show()
        }

    }

}