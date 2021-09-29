    package com.example.ocrapp.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.ocrapp.R
import com.example.ocrapp.databinding.FragmentSplashScreenBinding
import com.example.ocrapp.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class SplashScreenFragment : Fragment() {

    lateinit var binding: FragmentSplashScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({

            val user = FirebaseAuth.getInstance().currentUser
            val firestore = FirebaseFirestore.getInstance()

            if (user == null) {

                Navigation.findNavController(binding.logo)
                    .navigate(R.id.action_splashScreenFragment_to_loginFragment)

            } else {

                firestore.collection("users").document(user.uid).get().addOnSuccessListener {

                    val logged = it.toObject<User>()

                    if (logged?.type.equals("customer")) {
                        Navigation.findNavController(binding.logo)
                            .navigate(R.id.action_splashScreenFragment_to_customerActivity)
                    } else {
                        Navigation.findNavController(binding.logo)
                            .navigate(R.id.action_splashScreenFragment_to_enterpriseActivity)
                    }

                    requireActivity().finishAffinity()

                }.addOnFailureListener {

                    Snackbar.make(binding.logo, "Ha ocurrido un error", Snackbar.LENGTH_INDEFINITE)
                        .show()

                }


            }


        }, 1500)

    }
}