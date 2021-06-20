package com.example.ocrapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ocrapp.databinding.AdapterGroupsBinding
import com.example.ocrapp.model.Group

class GroupAdapter() : RecyclerView.Adapter<GroupAdapter.GroupHolder>() {

    val groups = mutableListOf<Group>()

    class GroupHolder(val binding: AdapterGroupsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {

        val group = groups[position]
        holder.binding.TVNameGroup.text = group.name
        holder.binding.TVNumberMeasures.text = "Mediciones: 0"

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        return GroupHolder(
            AdapterGroupsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}