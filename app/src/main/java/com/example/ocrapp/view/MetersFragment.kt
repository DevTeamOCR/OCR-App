package com.example.ocrapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocrapp.R
import com.example.ocrapp.adapter.MeterAdapter
import com.example.ocrapp.databinding.FragmentMetersBinding
import com.example.ocrapp.viewmodel.MetersViewModel

class MetersFragment : Fragment() {

    private lateinit var binding: FragmentMetersBinding
    private lateinit var adapter: MeterAdapter
    private lateinit var viewModel: MetersViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MetersViewModel::class.java)

        adapter = MeterAdapter()

        binding.rvMeters.adapter = adapter
        binding.rvMeters.layoutManager = LinearLayoutManager(requireContext())

        val separator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        binding.rvMeters.addItemDecoration(separator)

        viewModel.getMeters(adapter, binding.TVMessageComsumption1)

        binding.floatingAddMeter.setOnClickListener {

        context?.let {
            AddMeterDialog(it,this, adapter).show()
        }

        }

    }



}