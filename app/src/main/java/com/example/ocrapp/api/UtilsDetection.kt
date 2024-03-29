package com.example.ocrapp.api

import com.example.ocrapp.model.Detection
import kotlin.math.abs

class UtilsDetection(val response: DetectionResponse) {

    // Getting detections readable from the response.
    fun convert(): MutableList<Detection> {

        // List of boxes with corresponding scores and classes.
        val detections = mutableListOf<Detection>()

        // Getting the correct lists from responses (there are some lists inside lists)
        val boxes = response.boxes[0]
        val scores = response.scores[0]
        val classes = response.classes[0]

        // Getting boxes
        for ((index, box) in boxes.withIndex()) {

            var xmin = 0.0
            var ymin = 0.0
            var xmax = 0.0
            var ymax = 0.0

            // Getting positions for each box
            for ((indexPos, pos) in box.withIndex()) {

                when (indexPos) {
                    0 -> xmin = pos
                    1 -> ymin = pos
                    2 -> xmax = pos
                    3 -> ymax = pos
                }
            }

            // Box with corresponding score and class
            val myBox = Box(xmin, ymin, xmax, ymax)
            val score = Score(scores[index])
            val clazz = Class(classes[index])

            // Adding the box with score and a class
            detections.add(Detection(myBox, score, clazz))

        }
        return detections
    }

    fun sortDetectionsByBoxXmin(): List<Detection> {

        val detections = convert()

        // Sort detections by boxes with xmin variable, this is for have boxes sorted from left to right
        detections.sortBy { it.box.xmin }

        return detections

    }

    fun consumptionDetected(reliability: Double): Int {

        var numberString = "0"
        val detectionsSortedByBoxXmin = sortDetectionsByBoxXmin()

        for (detection in detectionsSortedByBoxXmin) {

            if (detection.clazz.value != 11.0) {

                if (detection.score.value >= reliability) {

                    numberString += if (detection.clazz.value == 10.0) {
                        0
                    } else {
                        detection.clazz.value.toInt()
                    }
                }
            }
        }

        return numberString.toInt()
    }

    fun meanReliability(reliability: Double): Double{

        var mean = 0.0
        var counter = 0.0

        val sortedDetections = sortDetectionsByBoxXmin()

        for(detection in sortedDetections){

            if(detection.score.value >= reliability){

                mean.plus(detection.score.value)
                counter.plus(1)

            }
        }

        return mean.div(counter)

    }

    fun computeConsumption(previous: Int, current: Int): Int{

        return if(current < previous){
            abs(99999-previous-current)
        }else{
            current - previous
        }

    }

    fun computeCostConsumption(consumption: Int, rate: Double): Double{
        return consumption.times(rate)
    }

}