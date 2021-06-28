package com.example.ocrapp.util

import androidx.core.widget.addTextChangedListener
import com.example.ocrapp.model.Consumption
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.abs

object AppUtils {

    fun getText(txtInputLayout: TextInputLayout): String {
        return txtInputLayout.editText?.text.toString()
    }

    fun setText(txtInputLayout: TextInputLayout, text: String) {
        txtInputLayout.editText?.setText(text)
    }

    fun putError(txtInputLayout: TextInputLayout, error: String) {
        txtInputLayout.error = error
    }

    fun removeError(txtInputLayout: TextInputLayout) {
        txtInputLayout.error = ""
        txtInputLayout.isErrorEnabled = false
    }

    fun listenerError(txtInputLayout: TextInputLayout) {
        txtInputLayout.editText?.addTextChangedListener(
            afterTextChanged = {
                if (getText(txtInputLayout).isNotEmpty())
                    removeError(txtInputLayout)
            }
        )
    }

    fun listenerCheckPassword(txtPass: TextInputLayout): Boolean {

        val pass = getText(txtPass)

        if (pass.length > 6) {
            if (pass.matches(".*\\d.*".toRegex()) && pass.matches(".*[a-z].*".toRegex()) && pass.matches(
                    ".*[A-Z].*".toRegex()
                )
            ) {
                removeError(txtPass)
                return true
            } else {
                putError(txtPass, "Debe contener mayusculas, minusculas y números")
            }
        } else {
            putError(txtPass, "Debe contener al menos 6 caracteres")
        }

        return false

    }

    fun computeConsumption(previous: Double, current: Double): Number{

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