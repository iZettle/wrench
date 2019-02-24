package com.izettle.wrench.dialogs.booleanvalue

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.izettle.wrench.R
import kotlinx.android.synthetic.main.fragment_boolean_value.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class BooleanValueFragment : DialogFragment() {

    private val viewModel: FragmentBooleanValueViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        assert(arguments != null)

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_boolean_value, null)

        val args = BooleanValueFragmentArgs.fromBundle(arguments!!)

        viewModel.init(args.configurationId, args.scopeId)

        viewModel.viewState.observe(this, Observer { viewState ->
            if (viewState != null) {
                val invisible = (view.container.visibility == View.INVISIBLE)
                if (view.container.visibility == View.INVISIBLE && viewState.title != null) {
                    view.container.visibility = View.VISIBLE
                }
                view.title.text = viewState.title
                view.value.isChecked = viewState.enabled ?: false
                if (invisible) {
                    view.value.jumpDrawablesToCurrentState()
                }

                if (viewState.saving || viewState.reverting) {
                    view.value.isEnabled = false
                    view.save.isEnabled = false
                    view.revert.isEnabled = false
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

        view.revert.setOnClickListener {
            viewModel.revertClick()
        }

        view.value.setOnCheckedChangeListener { _, isChecked -> viewModel.checkedChanged(isChecked) }

        view.save.setOnClickListener {
            viewModel.saveClick(view.value.isChecked.toString())
        }

        return AlertDialog.Builder(requireActivity())
                .setView(view)
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
