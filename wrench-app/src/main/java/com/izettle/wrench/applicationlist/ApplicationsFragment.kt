package com.izettle.wrench.applicationlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.izettle.wrench.databinding.FragmentApplicationsBinding
import com.izettle.wrench.di.Injectable
import javax.inject.Inject


class ApplicationsFragment : Fragment(), Injectable {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var fragmentApplicationsBinding: FragmentApplicationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentApplicationsBinding = FragmentApplicationsBinding.inflate(inflater, container, false)
        fragmentApplicationsBinding.list.layoutManager = LinearLayoutManager(context)
        return fragmentApplicationsBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val model: ApplicationViewModel = ViewModelProviders.of(this, viewModelFactory).get(ApplicationViewModel::class.java)

        fragmentApplicationsBinding.list.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, true)

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
