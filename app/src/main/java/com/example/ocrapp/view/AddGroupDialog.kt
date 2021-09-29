package com.example.ocrapp.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.ocrapp.adapter.GroupAdapter
import com.example.ocrapp.databinding.DialogAddGroupBinding
import com.example.ocrapp.util.AppUtils
import com.example.ocrapp.viewmodel.GroupsViewModel

class AddGroupDialog(
    context: Context,
    groupsEnterpriseFragment: GroupsEnterpriseFragment,
    adapter: GroupAdapter
) : Dialog(context) {

    private lateinit var binding: DialogAddGroupBinding
    private lateinit var viewModel: GroupsViewModel
    private val groups = groupsEnterpriseFragment
    private val adapter = adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogAddGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        viewModel = ViewModelProvider(groups.requireActivity()).get(GroupsViewModel::class.java)

        binding.apply {
            btnAddGroup.setOnClickListener {
                val name = AppUtils.getText(tfNameGroup)

                viewModel.createGroup(name, tfNameGroup, adapter)

                dismiss()


            }

            btnClose.setOnClickListener { cancel() }
        }
    }

}