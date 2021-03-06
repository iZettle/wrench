package com.izettle.wrench.dialogs.booleanvalue

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.izettle.wrench.R
import com.izettle.wrench.databinding.FragmentBooleanValueBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BooleanValueFragment : DialogFragment() {

    private lateinit var binding: FragmentBooleanValueBinding
    private val viewModel: FragmentBooleanValueViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        assert(arguments != null)

        binding = FragmentBooleanValueBinding.inflate(LayoutInflater.from(context))

        val args = BooleanValueFragmentArgs.fromBundle(arguments!!)

        viewModel.init(args.configurationId, args.scopeId)

        viewModel.configuration.observe(this, Observer { wrenchConfiguration ->
            if (wrenchConfiguration != null) {
                requireDialog().setTitle(wrenchConfiguration.key)
            }
        })

        viewModel.selectedConfigurationValueLiveData.observe(this, Observer { wrenchConfigurationValue ->
            viewModel.selectedConfigurationValue = wrenchConfigurationValue
            if (wrenchConfigurationValue != null) {
                binding.value.isChecked = java.lang.Boolean.valueOf(wrenchConfigurationValue.value)
            }
        })

        return AlertDialog.Builder(requireActivity())
                .setTitle(".")
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok
                ) { _, _ ->
                    viewModel.updateConfigurationValue(binding.value.isChecked.toString())
                    dismiss()
                }
                .setNegativeButton(R.string.revert
                ) { _, _ ->
                    if (viewModel.selectedConfigurationValue != null) {
                        viewModel.deleteConfigurationValue()
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
