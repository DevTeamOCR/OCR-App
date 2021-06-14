package com.example.ocrapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ocrapp.databinding.FragmentLoginBinding
import com.example.ocrapp.util.AppUtils
import com.example.ocrapp.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)


        binding.btnForgotPasswordLogin.setOnClickListener{
            viewModel.goToForgotPassword(it)
        }

        binding.btnLogin.setOnClickListener{
            viewModel.login(
                AppUtils.getText(binding.tfEmailLogin),
                AppUtils.getText(binding.tfPasswordLogin), it, this
            )
        }


        binding.btnRegisterHere.setOnClickListener {
            viewModel.goToRegister(it)
        }


    }

}