package com.example.ocrapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ocrapp.R
import com.example.ocrapp.databinding.FragmentMetersBinding

class MetersFragment : Fragment() {

    private lateinit var binding: FragmentMetersBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMetersBinding.inflate(inflater,container,false)
        return binding.root

    }
}