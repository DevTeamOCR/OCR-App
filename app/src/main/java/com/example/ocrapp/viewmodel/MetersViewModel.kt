package com.example.ocrapp.viewmodel

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ocrapp.adapter.MeterAdapter
import com.example.ocrapp.model.Meter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class MetersViewModel: ViewModel() {

    val meters = MutableLiveData<List<Meter>>()
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    fun getMeters(adapter: MeterAdapter, noMeters: TextView){

        val user = auth.currentUser

        if (user != null) {
            firestore.collection("users").document(user.uid).collection("meters")
                .get().addOnCompleteListener { queryMeters ->

                    if(queryMeters.isSuccessful){

                        for(document in queryMeters.result!!){
                            val meter = document.toObject<Meter>()
                            Log.d("Meter", "$meter")
                            adapter.meters.add(meter)
                        }
                    }

                    adapter.notifyDataSetChanged()

                    if(adapter.itemCount > 0)
                        noMeters.visibility = View.GONE
                    else
                        noMeters.visibility = View.VISIBLE

                }.addOnFailureListener {
                    Log.e("Error Meters: ", "Error getting meters")
                }
        }
    }

    fun addMeter(name: String, serial: String, view: View, adapter: MeterAdapter){

        val user = auth.currentUser

        user?.let {

            val document =
                firestore.collection("users").document(it.uid).collection("meters").document()

            val meter = Meter(document.id, name, serial)

            adapter.meters.add(meter)
            adapter.notifyDataSetChanged()
            document.set(meter).addOnCompleteListener { addMeter ->
                if(addMeter.isSuccessful){
                    Snackbar.make(view,"Se ha agregado el medidor", Snackbar.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { fail ->
                Log.e("Error add meter:", "Error adding meter from meters fragment", fail)
                Snackbar.make(view, "Ha ocurrido un error al agregar el medidor", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


}

