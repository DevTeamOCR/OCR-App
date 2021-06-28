package com.example.ocrapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.example.ocrapp.databinding.FragmentCustomerFeedBinding
import com.example.ocrapp.model.Consumption
import com.example.ocrapp.model.Meter
import com.example.ocrapp.util.AppUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

class CustomerFeed : Fragment() {

    private lateinit var binding: FragmentCustomerFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomerFeedBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getConsumptions()
    }

    private fun buildChart(consumptions: List<Consumption>){
        val cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.title("Consumos")

        cartesian.yAxis(0).title("Consumo (KWh)")
        cartesian.xAxis(0).title("Consumo #")

        cartesian.crosshair().enabled(true)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        Log.e("consumptions",consumptions.toString())

        val entries = ArrayList<DataEntry>()

        for((index,consumption) in consumptions.withIndex()){
            if(index + 1 < consumptions.size){

                entries.add(ValueDataEntry(index+1, AppUtils.computeConsumption(consumptions[index].value,consumptions[index+1].value)))
                Log.e("bug:","Prev: ${consumptions[index].value} Current: ${consumptions[index+1].value}")
            }
        }


        val set = Set.instantiate()
        set.data(entries)
        val seriesMapping = set.mapAs("{x: 'x', value: 'value' }")

        val serie = cartesian.line(seriesMapping)
        serie.hovered().markers().enabled(true)
        serie.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4)
        serie.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5)
            .offsetY(5)

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13)
        cartesian.legend().padding(0,0,10,0)

        binding.lineChart.setChart(cartesian)
    }

    fun getConsumptions(){

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        var meter: Meter = Meter()
        val listConsumption = mutableListOf<Consumption>()

        auth.currentUser?.let {
            firestore.collection("users").document(it.uid).collection("meters").get()
                .addOnCompleteListener { queryMeters ->

                    if(queryMeters.isSuccessful){
                        for(document in queryMeters.result!!){
                            meter = document.toObject()
                        }


                        firestore.collection("users").document(it.uid).collection("meters").document(
                            meter.name).collection("consumptions").orderBy("timestamp").limit(12).get()
                            .addOnCompleteListener { consumptions ->

                                for(document in consumptions.result!!){
                                    listConsumption.add(document.toObject())
                                }

                                buildChart(listConsumption)

                            }.addOnFailureListener {  }

                    }
                }
                .addOnFailureListener {  }
        }



    }

}