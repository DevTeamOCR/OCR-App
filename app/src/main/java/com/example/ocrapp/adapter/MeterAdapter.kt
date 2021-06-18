package com.example.ocrapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ocrapp.databinding.AdapterMeterBinding
import com.example.ocrapp.model.Meter

class MeterAdapter(private val meters: ArrayList<Meter>): RecyclerView.Adapter<MeterAdapter.MeterHolder>(){

    class MeterHolder(val binding : AdapterMeterBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = meters.size

    override fun onBindViewHolder(holder: MeterHolder, position: Int) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeterHolder {
        return MeterHolder(AdapterMeterBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }
}