package com.example.ocrapp.util

import com.google.android.material.textfield.TextInputLayout
import java.util.*

object AppUtils {


    @JvmStatic
    fun getText(tf: TextInputLayout): String {
        return Objects.requireNonNull(tf.editText)!!.text.toString()
    }


    @JvmStatic
    fun setText(tf: TextInputLayout, text: String) {
        Objects.requireNonNull(tf.editText)!!.setText(text)
    }

}