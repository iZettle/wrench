package com.izettle.wrench.dialogs.scope

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.izettle.wrench.R
import com.izettle.wrench.database.WrenchScope
import com.izettle.wrench.databinding.FragmentScopeBinding
import com.izettle.wrench.di.Injectable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScopeFragment : DialogFragment(), ScopeRecyclerViewAdapter.Listener, Injectable {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentScopeBinding
    private lateinit var viewModel: ScopeFragmentViewModel
    private var adapter: ScopeRecyclerViewAdapter? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        assert(arguments != null)

        binding = FragmentScopeBinding.inflate(LayoutInflater.from(context))

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ScopeFragmentViewModel::class.java)

        viewModel.init(arguments!!.getLong(ARGUMENT_APPLICATION_ID))

        viewModel.scopes.observe(this, Observer { scopes -> adapter!!.submitList(scopes) })

        viewModel.selectedScopeLiveData.observe(this, Observer { wrenchScope -> viewModel.selectedScope = wrenchScope })

        adapter = ScopeRecyclerViewAdapter(this)
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        return AlertDialog.Builder(requireContext())
                .setTitle(R.string.select_scope)
                .setView(binding.root)
                .setPositiveButton("Add") { _, _ ->

                    val input = EditText(requireContext())
                    input.setSingleLine()

                    AlertDialog.Builder(requireContext())
                            .setTitle("Create new scope")
                            .setView(input)
                            .setPositiveButton("OK"
                            ) { _, _ ->
                                val scopeName = input.text.toString()
                                AsyncTask.execute { viewModel.createScope(scopeName) }
                            }.setNegativeButton("Cancel", null)
                            .show()
                }
                .setNegativeButton("Delete"
                ) { _, _ ->
                    val selectedScope = viewModel.selectedScope
                    if (selectedScope != null) {
                        if (!WrenchScope.isDefaultScope(selectedScope)) {
                            AsyncTask.execute { viewModel.removeScope(selectedScope) }
                        }
                    }
                }
                .create()
    }

    override fun onClick(view: View, wrenchScope: WrenchScope) {
        CoroutineScope(Dispatchers.Default).launch { viewModel.selectScope(wrenchScope) }
        dismiss()
    }

    companion object {

        private const val ARGUMENT_APPLICATION_ID = "ARGUMENT_APPLICATION_ID"

        fun newInstance(applicationId: Long): ScopeFragment {
            val fragment = ScopeFragment()
            val args = Bundle()
            args.putLong(ARGUMENT_APPLICATION_ID, applicationId)
            fragment.arguments = args
            return fragment
        }
    }
}
