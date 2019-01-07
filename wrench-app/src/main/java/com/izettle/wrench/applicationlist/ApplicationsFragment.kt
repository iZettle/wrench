package com.izettle.wrench.applicationlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.izettle.wrench.databinding.FragmentApplicationsBinding
import org.koin.androidx.viewmodel.ext.viewModel


class ApplicationsFragment : Fragment() {

    private val model: ApplicationViewModel by viewModel()

    private lateinit var fragmentApplicationsBinding: FragmentApplicationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentApplicationsBinding = FragmentApplicationsBinding.inflate(inflater, container, false)
        fragmentApplicationsBinding.list.layoutManager = LinearLayoutManager(context)
        return fragmentApplicationsBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragmentApplicationsBinding.list.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ApplicationAdapter()
        model.applications.observe(this, Observer { adapter.submitList(it) })
        fragmentApplicationsBinding.list.adapter = adapter

        model.isListEmpty.observe(this, Observer { isEmpty ->
            val animator = fragmentApplicationsBinding.animator
            if (isEmpty == null || isEmpty) {
                animator.displayedChild = animator.indexOfChild(fragmentApplicationsBinding.noApplicationsEmptyView)
            } else {
                animator.displayedChild = animator.indexOfChild(fragmentApplicationsBinding.list)
            }
        })
    }
}
