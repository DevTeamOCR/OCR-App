package com.example.ocrapp.api

import junit.framework.TestCase
import com.example.ocrapp.util.AppUtils.computeConsumption
import com.example.ocrapp.util.AppUtils.computeCostConsumption

class UtilsDetectionTest : TestCase() {
    fun testComputeCostConsumption() {
        var xd= computeConsumption(7015.0, 7050.0)
        var value=computeCostConsumption(xd.toInt(), 595.64)
        val solution = String.format("%.1f", value)

        assertEquals("20847,4", solution)
    }

    fun testComputeCostConsumptionArray(){
        val previousRead= arrayOf<Double>(3508.0, 6162.0, 9312.0, 12521.0, 8416.0, 10889.0, 9771.0, 7825.0, 6893.0, 7891.0, 2905.0, 11670.0, 1041.0, 4239.0, 1760.0, 7015.0, 9461.0, 5827.0, 5943.0, 2949.0)
        val currentRead= arrayOf<Double>(3580.0, 6200.0, 9500.0, 12700.0, 8500.0, 11000.0, 9880.0, 8000.0, 6900.0, 7900.0, 3000.0, 11900.0, 2000.0, 4400.0, 1860.0, 7300.0, 9600.0, 5930.0, 6100.0, 3100.0)
        val computedReadings= mutableListOf<Double>()
        val computedCostReadings= mutableListOf<String>()
        val expectedReadings = mutableListOf<String>("42886,08", "22634,32", "111980,32","106619,56", "50033,76", "66116,04", "64924,76", "104237,00", "4169,48", "5360,76", "56585,80", "136997,20", "571218,76" , "95898,04", "59564,00", "169757,40", "82793,96", "61350,92", "93515,48", "89941,64")

        val things=0
        for (double in previousRead.indices){
            computedReadings += (computeConsumption(previousRead.get(double),currentRead.get(double))).toDouble()
        }

        for (double in computedReadings.indices){
            val value= computeCostConsumption(computedReadings.get(double).toInt(),595.64)
            val solution = String.format("%.2f", value)
            computedCostReadings += solution
        }

        assertEquals(expectedReadings, computedCostReadings)
    }


}

