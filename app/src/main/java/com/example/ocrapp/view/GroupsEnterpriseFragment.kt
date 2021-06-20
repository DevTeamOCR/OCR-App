package com.example.ocrapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocrapp.adapter.GroupAdapter
import com.example.ocrapp.databinding.FragmentGroupsEnterpriseBinding
import com.example.ocrapp.viewmodel.GroupsViewModel

class GroupsEnterpriseFragment : Fragment() {

    private lateinit var binding: FragmentGroupsEnterpriseBinding
    private lateinit var adapter: GroupAdapter
    private lateinit var viewModel: GroupsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGroupsEnterpriseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(GroupsViewModel::class.java)

        adapter = GroupAdapter()

        binding.rvGroups.adapter = adapter
        binding.rvGroups.layoutManager = LinearLayoutManager(requireContext())

        val separator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        binding.rvGroups.addItemDecoration(separator)

        viewModel.getGroups(adapter, binding.TVMessageGroups)

        binding.floatingAddGroup.setOnClickListener {

            context?.let {
                AddGroupDialog(it, this, adapter).show()
            }

        }

    }

}