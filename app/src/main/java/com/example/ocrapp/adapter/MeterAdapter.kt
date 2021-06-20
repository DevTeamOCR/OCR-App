package com.example.ocrapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ocrapp.databinding.AdapterMeterBinding
import com.example.ocrapp.model.Meter

class MeterAdapter(): RecyclerView.Adapter<MeterAdapter.MeterHolder>(){

    val meters = mutableListOf<Meter>()

    class MeterHolder(val binding : AdapterMeterBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = meters.size

    override fun onBindViewHolder(holder: MeterHolder, position: Int) {

        val meter = meters[position]

        holder.binding.TVNameMeter.text = meter.name
        holder.binding.TVNumberComsumptions.text = "Consumos: 0"

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeterHolder {
        return MeterHolder(AdapterMeterBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }
}