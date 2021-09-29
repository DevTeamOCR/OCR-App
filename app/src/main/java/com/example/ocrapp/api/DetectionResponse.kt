package com.example.ocrapp.api

data class DetectionResponse(
    val boxes: List<List<List<Double>>>,
    val classes: List<List<Double>>,
    val scores: List<List<Double>>
)