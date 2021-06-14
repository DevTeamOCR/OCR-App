package com.example.ocrapp.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.ocrapp.R
import com.example.ocrapp.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val user: MutableLiveData<User> = MutableLiveData()
    fun setUser(value: User){
        user.value = value
    }

    fun register(pass: String, view: View){

        Snackbar.make(view,"Registrando...",Snackbar.LENGTH_SHORT).show()

        val myUser = user.value

        myUser?.let { theUser ->
            auth.createUserWithEmailAndPassword(theUser.email, pass).addOnSuccessListener {

                theUser.uid = auth.uid

                firestore.collection("users").document(theUser.uid.toString()).set(theUser).addOnSuccessListener {

                    if(theUser.type == "customer"){
                        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_customerActivity)
                    }else{
                        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_enterpriseActivity)
                    }
                }

            }.addOnFailureListener{ fail ->
                Snackbar.make(view, fail.message.toString(), Snackbar.LENGTH_SHORT).show();
            }
        }
    }


}