package com.example.ocrapp.view

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ocrapp.R
import com.example.ocrapp.databinding.FragmentRegisterBinding
import com.example.ocrapp.model.User
import com.example.ocrapp.util.AppUtils
import com.example.ocrapp.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {


    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)

        fillTypes()
        listeners()

        binding.apply {

            tfPassword.apply {
                helperText = "Debe contener al menos 6 caracteres"
            }

            btnRegister.setOnClickListener{
                if(verifyRegister()){
                    updateUser()
                    viewModel.register(AppUtils.getText(tfPassword),it)
                }
            }

        }

    }

    private fun fillTypes(){
        val types = arrayOf(getString(R.string.customer),getString(R.string.enterprise))
        val adapter = ArrayAdapter(requireContext(),R.layout.dropdown_menu_popup_item,types)
        binding.acTvTypeMenu.inputType = InputType.TYPE_NULL
        binding.acTvTypeMenu.keyListener = null
        binding.acTvTypeMenu.setAdapter(adapter)
    }

    private fun updateUser(){

        binding.apply {

            val name = AppUtils.getText(tfName)
            val email = AppUtils.getText(tfEmail)
            var type = AppUtils.getText(tfUserType)

            if(type == "Cliente"){
                type = "customer"
            }else{
               type = "enterprise"
            }

            val user = User(name = name, email = email, type = type)
            viewModel.setUser(user)
        }

    }

    private fun verifyRegister(): Boolean{

        var checked = true

        binding.apply {
            tfName.apply {
                if (AppUtils.getText(this).isEmpty()) {
                    checked = false
                    AppUtils.putError(this, "Ingrese su nombre")
                }
            }

            tfEmail.apply {
                if (AppUtils.getText(this).isEmpty()) {
                    checked = false
                    AppUtils.putError(this, "Ingrese su correo")
                }
            }

            tfUserType.apply {
                if (AppUtils.getText(this).isEmpty()) {
                    checked = false
                    AppUtils.putError(this, "Debes seleccionar un tipo de usuario")
                }
            }

            checked = AppUtils.listenerCheckPassword(tfPassword)

            val pass = AppUtils.getText(tfPassword)
            val confPassword = AppUtils.getText(tfConfirmPassword)

            if(pass != confPassword){
                checked = false
            }
        }

        return checked
    }

    private fun listeners(){

        binding.apply {
            AppUtils.listenerError(tfName)
            AppUtils.listenerError(tfEmail)
            AppUtils.listenerError(tfUserType)

            tfConfirmPassword.editText?.addTextChangedListener(
                afterTextChanged = {

                    val pass = AppUtils.getText(tfPassword)
                    val confPass = AppUtils.getText(tfConfirmPassword)

                    if(pass != confPass){
                        AppUtils.putError(tfConfirmPassword,"Las contraseñas no coinciden")
                    }else{
                        AppUtils.removeError(tfConfirmPassword)
                    }
                }
            )

            tfPassword.editText?.addTextChangedListener(

                afterTextChanged = {

                    if(AppUtils.listenerCheckPassword(tfPassword)){
                        AppUtils.removeError(tfPassword)
                    }
                }

            )
        }
    }

}