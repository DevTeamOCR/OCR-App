package com.example.ocrapp.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.ocrapp.R
import com.example.ocrapp.databinding.DialogConsumptionBinding
import com.example.ocrapp.model.Meter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ConsumptionDetectedDialog(context: Context,var value: Int): Dialog(context) {

    private lateinit var binding: DialogConsumptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogConsumptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.editConsumption.setText("$value")

        getMeters()

        binding.apply {

            btnRetry.setOnClickListener {  }

            btnConfirm.setOnClickListener {  }

        }

    }


    private fun getMeters(){

        val user = FirebaseAuth.getInstance().currentUser
        val firestore = FirebaseFirestore.getInstance()

        val listMeters = ArrayList<Meter>()

        user?.let {
            firestore.collection("users").document(it.uid).collection("meters").get()
                .addOnCompleteListener { meters ->

                    for(document in meters.result!!){

                        val meter = document.toObject<Meter>()

                        listMeters.add(meter)
                    }

                    val array = listMeters.toArray()
                    val adapter = ArrayAdapter(context, R.layout.dropdown_menu_popup_item, array)

                    binding.menuMeters.adapter = adapter

                }
                .addOnFailureListener { error -> Log.e("Error meters", "Fail", error) }
        }

    }



}