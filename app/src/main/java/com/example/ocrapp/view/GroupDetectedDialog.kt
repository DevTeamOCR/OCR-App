package com.example.ocrapp.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.ocrapp.R
import com.example.ocrapp.databinding.DialogGroupDetectedBinding
import com.example.ocrapp.model.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class GroupDetectedDialog(context: Context, var value: Int) : Dialog(context) {

    private lateinit var binding: DialogGroupDetectedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogGroupDetectedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.editConsumption.setText("$value")

        getGroups()

        binding.apply {

            btnRetry.setOnClickListener { }

            btnConfirm.setOnClickListener { }

        }

    }

    private fun getGroups() {

        val user = FirebaseAuth.getInstance().currentUser
        val firestore = FirebaseFirestore.getInstance()

        val listGroups = ArrayList<Group>()

        user?.let {
            firestore.collection("users").document(it.uid).collection("groups").get()
                .addOnCompleteListener { groups ->

                    for (document in groups.result!!) {

                        val group = document.toObject<Group>()

                        listGroups.add(group)
                    }

                    val array = listGroups.toArray()
                    val adapter = ArrayAdapter(context, R.layout.dropdown_menu_popup_item, array)

                    binding.menuGroup.adapter = adapter

                }
                .addOnFailureListener { error -> Log.e("Error groups", "Fail", error) }
        }

    }

}