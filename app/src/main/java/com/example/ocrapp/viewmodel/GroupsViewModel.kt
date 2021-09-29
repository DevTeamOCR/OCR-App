package com.example.ocrapp.viewmodel

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ocrapp.adapter.GroupAdapter
import com.example.ocrapp.model.Group
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class GroupsViewModel : ViewModel() {

    val groups = MutableLiveData<List<Group>>()
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    fun getGroups(adapter: GroupAdapter, noGroups: TextView) {

        val enterprise = auth.currentUser

        if (enterprise != null) {
            firestore.collection("users").document(enterprise.uid).collection("groups")
                .get().addOnCompleteListener { queryMeters ->

                    if (queryMeters.isSuccessful) {

                        for (document in queryMeters.result!!) {
                            val group = document.toObject<Group>()
                            Log.d("Group", "$group")
                            adapter.groups.add(group)
                        }
                    }

                    adapter.notifyDataSetChanged()

                    if (adapter.itemCount > 0)
                        noGroups.visibility = View.GONE
                    else
                        noGroups.visibility = View.VISIBLE

                }.addOnFailureListener {
                    Log.e("Error Groups: ", "Error getting groups")
                }
        }
    }

    fun createGroup(name: String, view: View, adapter: GroupAdapter) {

        val enterprise = auth.currentUser

        enterprise?.let {

            val group = Group(name, name)
            val document =
                firestore.collection("users").document(it.uid).collection("groups")
                    .document(group.name)

            adapter.groups.add(group)
            adapter.notifyDataSetChanged()
            document.set(group).addOnCompleteListener { createGroup ->
                if (createGroup.isSuccessful) {
                    Snackbar.make(view, "Se ha creado el grupo", Snackbar.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { fail ->
                Log.e("Error create group:", "Error creating groups from groups fragment", fail)
                Snackbar.make(view, "Ha ocurrido un error al crear el grupo", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

}