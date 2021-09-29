package com.example.ocrapp.model

data class Group(var id: String = "", var name: String = "") {

    override fun toString(): String {
        return name
    }

}