package com.example.ocrapp.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.ocrapp.adapter.MeterAdapter
import com.example.ocrapp.databinding.DialogAddMeterBinding
import com.example.ocrapp.util.AppUtils
import com.example.ocrapp.viewmodel.MetersViewModel

class AddMeterDialog(context: Context, metersFragment: MetersFragment, adapter: MeterAdapter): Dialog(context) {

    private lateinit var binding: DialogAddMeterBinding
    private lateinit var viewModel: MetersViewModel
    private val meters = metersFragment
    private val adapter = adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogAddMeterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        viewModel = ViewModelProvider(meters.requireActivity()).get(MetersViewModel::class.java)

        binding.apply {

            btnAddMeter.setOnClickListener {

                val name = AppUtils.getText(tfNameMeter)
                val serial = AppUtils.getText(tfSerialMeter)

                viewModel.addMeter(name,serial,tfNameMeter, adapter)

                dismiss()
            }

            btnClose.setOnClickListener { cancel() }
        }

    }



}