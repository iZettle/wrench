package com.izettle.wrench.dialogs.stringvalue

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.izettle.wrench.R
import com.izettle.wrench.databinding.FragmentStringValueBinding
import com.izettle.wrench.di.Injectable
import javax.inject.Inject

class StringValueFragment : DialogFragment(), Injectable {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentStringValueBinding
    private lateinit var viewModel: FragmentStringValueViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        assert(arguments != null)

        binding = FragmentStringValueBinding.inflate(LayoutInflater.from(context), null)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FragmentStringValueViewModel::class.java)

        val args = StringValueFragmentArgs.fromBundle(arguments!!)

        viewModel.init(args.configurationId, args.scopeId)

        viewModel.configuration.observe(this, Observer { wrenchConfiguration ->
            if (wrenchConfiguration != null) {
                dialog?.setTitle(wrenchConfiguration.key)
            }
        })

        viewModel.selectedConfigurationValueLiveData.observe(this, Observer { wrenchConfigurationValue ->
            viewModel.selectedConfigurationValue = wrenchConfigurationValue
            if (wrenchConfigurationValue != null) {
                binding.value.setText(wrenchConfigurationValue.value)
            }
        })

        binding.value.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                AsyncTask.execute { viewModel.updateConfigurationValue(binding.value.text!!.toString()) }
                dismiss()
            }
            false
        }

        return AlertDialog.Builder(requireActivity())
                .setTitle(R.string.select_scope)
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok
                ) { _, _ ->
                    AsyncTask.execute { viewModel.updateConfigurationValue(binding.value.text!!.toString()) }
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

        fun newInstance(args: StringValueFragmentArgs): StringValueFragment {
            val fragment = StringValueFragment()
            fragment.arguments = args.toBundle()
            return fragment
        }
    }
}
