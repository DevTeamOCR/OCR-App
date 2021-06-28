package com.example.ocrapp.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.ocrapp.R
import com.example.ocrapp.databinding.DialogConsumptionBinding
import com.example.ocrapp.model.Consumption
import com.example.ocrapp.model.Meter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ConsumptionDetectedDialog(context: Context,var value: Int): Dialog(context) {

    private lateinit var binding: DialogConsumptionBinding
    val user = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogConsumptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.editConsumption.setText("$value")

        getMeters()

        binding.apply {

            btnRetry.setOnClickListener { cancel() }

            btnConfirm.setOnClickListener {
              addConsumption()


            }

        }

    }


    private fun getMeters(){

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


    private fun addConsumption(){

        val selected = binding.menuMeters.selectedItem.toString()

        user?.let {

            val doc = firestore.collection("users").document(it.uid).collection("meters")
                .document(selected).collection("consumptions").document()

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(System.currentTimeMillis())

            val rate = binding.editRate.text.toString()

            val rateNumber = if(rate.isEmpty()){
                0.0
            }else{
                rate.toDouble()
            }

            val consumption = Consumption(rateNumber,date,binding.editConsumption.text.toString().toDouble(),System.currentTimeMillis())

            doc.set(consumption).addOnCompleteListener { addConsumption ->

                if(addConsumption.isSuccessful){
                    dismiss()
                    Toast.makeText(context, "Se ha agregado el consumo", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener { error ->
                Log.e("Error Consumption", "No added", error)
            }

        }

    }

}