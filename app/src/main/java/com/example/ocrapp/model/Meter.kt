package com.example.ocrapp.model

data class Meter(var id: String = "", var name: String = "", var serial: String = "") {

    override fun toString(): String {
        return name
    }

}