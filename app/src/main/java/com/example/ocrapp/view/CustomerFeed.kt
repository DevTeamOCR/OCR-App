package com.example.ocrapp.view

import android.os.Bundle
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


        binding.apply {

            val cartesian = AnyChart.line()
            cartesian.animation(true)
            cartesian.title("Consumos")

            cartesian.yAxis(0).title("Consumo (KWh)")
            cartesian.xAxis(0).title("Mes")

            cartesian.crosshair().enabled(true)

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

            val entries = ArrayList<DataEntry>()
            entries.add(ValueDataEntry("01",125))
            entries.add(ValueDataEntry("02", 325))
            entries.add(ValueDataEntry("03", 240))
            entries.add(ValueDataEntry("04", 80))
            entries.add(ValueDataEntry("05", 520))
            entries.add(ValueDataEntry("06", 140))
            entries.add(ValueDataEntry("07", 200))
            entries.add(ValueDataEntry("08", 300))
            entries.add(ValueDataEntry("09", 148))
            entries.add(ValueDataEntry("10", 180))
            entries.add(ValueDataEntry("11", 700))
            entries.add(ValueDataEntry("12", 964))



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

            lineChart.setChart(cartesian)

        }

    }

}