package com.izettle.wrench.dialogs.enumvalue

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.izettle.wrench.R
import com.izettle.wrench.database.WrenchPredefinedConfigurationValue
import com.izettle.wrench.databinding.FragmentEnumValueBinding
import com.izettle.wrench.di.Injectable
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class EnumValueFragment : DialogFragment(), PredefinedValueRecyclerViewAdapter.Listener, Injectable {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentEnumValueBinding
    private lateinit var viewModel: FragmentEnumValueViewModel
    private lateinit var adapter: PredefinedValueRecyclerViewAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        assert(arguments != null)

        binding = FragmentEnumValueBinding.inflate(LayoutInflater.from(context), null)

        val args = EnumValueFragmentArgs.fromBundle(arguments!!)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FragmentEnumValueViewModel::class.java)

        viewModel.init(args.configurationId, args.scopeId)

        viewModel.configuration.observe(this, Observer { wrenchConfiguration ->
            if (wrenchConfiguration != null) {
                dialog.setTitle(wrenchConfiguration.key)
            }
        })

        viewModel.selectedConfigurationValueLiveData.observe(this, Observer { wrenchConfigurationValue ->
            if (wrenchConfigurationValue != null) {
                viewModel.selectedConfigurationValue = wrenchConfigurationValue
            }
        })

        adapter = PredefinedValueRecyclerViewAdapter(this)
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        viewModel.predefinedValues.observe(this, Observer { items ->
            if (items != null) {
                adapter.submitList(items)
            }
        })

        return AlertDialog.Builder(activity!!)
                .setTitle(R.string.select_scope)
                .setView(binding.root)
                .setNegativeButton(R.string.revert
                ) { _, _ ->
                    if (viewModel.selectedConfigurationValue != null) {
                        AsyncTask.execute { viewModel.deleteConfigurationValue() }
                    }
                    dismiss()
                }
                .create()
    }

    override fun onClick(view: View, item: WrenchPredefinedConfigurationValue) {
        CoroutineScope(Dispatchers.Default).launch { viewModel.updateConfigurationValue(item.value!!) }
        dismiss()
    }

    companion object {

        fun newInstance(args: EnumValueFragmentArgs): EnumValueFragment {
            val fragment = EnumValueFragment()
            fragment.arguments = args.toBundle()
            return fragment
        }
    }
}
