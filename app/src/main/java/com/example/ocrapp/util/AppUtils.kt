package com.example.ocrapp.util

import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import java.util.*

object AppUtils {

    fun getText(txtInputLayout: TextInputLayout): String{
        return txtInputLayout.editText?.text.toString()
    }

    fun setText(txtInputLayout: TextInputLayout, text: String){
        txtInputLayout.editText?.setText(text)
    }

    fun putError(txtInputLayout: TextInputLayout, error: String){
        txtInputLayout.error = error
    }

    fun removeError(txtInputLayout: TextInputLayout){
        txtInputLayout.error = ""
        txtInputLayout.isErrorEnabled = false
    }

    fun listenerError(txtInputLayout: TextInputLayout){
        txtInputLayout.editText?.addTextChangedListener(
            afterTextChanged = {
                if(getText(txtInputLayout).isNotEmpty())
                    removeError(txtInputLayout)
            }
        )
    }

    fun listenerCheckPassword(txtPass: TextInputLayout): Boolean{

        val pass = getText(txtPass)

        if(pass.length > 6){
            if(pass.matches(".*\\d.*".toRegex()) && pass.matches(".*[a-z].*".toRegex()) && pass.matches(".*[A-Z].*".toRegex())){
                removeError(txtPass)
                return true
            }else{
                putError(txtPass,"Debe contener mayusculas, minusculas y números")
            }
        }else{
            putError(txtPass, "Debe contener al menos 6 caracteres")
        }

        return false

    }

}