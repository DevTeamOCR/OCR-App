package com.example.ocrapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ocrapp.databinding.AdapterConsumptionsBinding
import com.example.ocrapp.model.Consumption
import com.example.ocrapp.util.AppUtils

class ConsumptionAdapter(): RecyclerView.Adapter<ConsumptionAdapter.ConsumptionHolder>() {

    var consumptions = mutableListOf<Consumption>()
    class ConsumptionHolder(val binding: AdapterConsumptionsBinding):RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = consumptions.size

    override fun onBindViewHolder(holder: ConsumptionHolder, position: Int) {
        val consumption = consumptions[position]
        holder.binding.TVNameMeter.text = "Consumo # ${position+1}"
        holder.binding.TVDateComsumption.text = consumption.date

        if(position >= 1 && position < consumptions.size){

            val prev = consumptions[position-1]
            val consum = AppUtils.computeConsumption(prev.value,consumption.value)
            val cost = AppUtils.computeCostConsumption(consum, consumption.rate)

            holder.binding.TVComsumption.text = "$${cost.toInt()}"

        }else{
            holder.binding.TVComsumption.visibility = View.GONE
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumptionHolder {
        return ConsumptionHolder(AdapterConsumptionsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
}