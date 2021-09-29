package com.example.ocrapp.model

data class Consumption(var rate: Double=0.0, var date: String="", var value: Double=0.0, var timestamp: Long=0) {

    override fun toString(): String {
        return "$value , Timestamp: $timestamp"
    }
}