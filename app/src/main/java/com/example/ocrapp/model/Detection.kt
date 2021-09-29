package com.example.ocrapp.model

import com.example.ocrapp.api.Box
import com.example.ocrapp.api.Class
import com.example.ocrapp.api.Score

class Detection(val box: Box, val score: Score, val clazz: Class) {

    override fun toString(): String {
        return "Box: $box, Score: $score, Class: $clazz \n"
    }
}