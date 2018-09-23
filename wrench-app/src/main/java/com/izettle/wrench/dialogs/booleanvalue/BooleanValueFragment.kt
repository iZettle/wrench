package com.izettle.wrench.dialogs.booleanvalue

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.izettle.wrench.R
import com.izettle.wrench.databinding.FragmentBooleanValueBinding
import com.izettle.wrench.di.Injectable
import javax.inject.Inject

class BooleanValueFragment : DialogFragment(), Injectable {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentBooleanValueBinding
    private lateinit var viewModel: FragmentBooleanValueViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        assert(arguments != null)

        binding = FragmentBooleanValueBinding.inflate(LayoutInflater.from(context), null)

        val args = BooleanValueFragmentArgs.fromBundle(arguments)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FragmentBooleanValueViewModel::class.java)

        viewModel.init(args.configurationId.toLong(), args.scopeId.toLong())

        viewModel.configuration.observe(this, Observer { wrenchConfiguration ->
            if (wrenchConfiguration != null) {
                dialog.setTitle(wrenchConfiguration.key)
            }
        })

        viewModel.selectedConfigurationValueLiveData.observe(this, Observer { wrenchConfigurationValue ->
            viewModel.selectedConfigurationValue = wrenchConfigurationValue
            if (wrenchConfigurationValue != null) {
                binding.value.isChecked = java.lang.Boolean.valueOf(wrenchConfigurationValue.value)
            }
        })

        return AlertDialog.Builder(requireActivity())
                .setTitle(R.string.select_scope)
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok
                ) { _, _ ->
                    AsyncTask.execute { viewModel.updateConfigurationValue(binding.value.isChecked.toString()) }
                    dismiss()
                }
                .setNegativeButton(R.string.revert
                ) { _, _ ->
                    if (viewModel.selectedConfigurationValue != null) {
                        AsyncTask.execute { viewModel.deleteConfigurationValue() }
                    }
                    dismiss()
                }
                .create()
    }

    companion object {

        fun newInstance(args: BooleanValueFragmentArgs): BooleanValueFragment {
            val fragment = BooleanValueFragment()
            fragment.arguments = args.toBundle()
            return fragment
        }
    }
}
