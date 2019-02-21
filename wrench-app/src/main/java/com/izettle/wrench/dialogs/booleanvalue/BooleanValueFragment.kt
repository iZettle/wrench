package com.izettle.wrench.dialogs.booleanvalue

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
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

        viewModel.viewState.observe(this, Observer { viewState ->
            if (viewState != null) {
                when (viewState) {
                    is ViewState.NewConfiguration -> {
                        requireDialog().setTitle(viewState.title)
                        binding.title.text = viewState.title
                    }
                    is ViewState.NewConfigurationValue -> binding.value.isChecked = viewState.enabled
                }
            }
        })

        viewModel.viewEffects.observe(this, Observer { viewEffect ->
            if (viewEffect != null) {
                viewEffect.getContentIfNotHandled()?.let { contentIfNotHandled ->
                    when (contentIfNotHandled) {
                        ViewEffect.Dismiss -> dismiss()
                    }
                }
            }
        })

        binding.revert.setOnClickListener {
            viewModel.revertClick()
        }

        binding.save.setOnClickListener {
            viewModel.saveClick(binding.value.isChecked.toString())
        }

        return AlertDialog.Builder(requireActivity())
                .setView(binding.root)
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
