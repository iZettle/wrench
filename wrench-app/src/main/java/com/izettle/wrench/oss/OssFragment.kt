package com.izettle.wrench.oss


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.izettle.wrench.databinding.FragmentOssBinding
import com.izettle.wrench.di.Injectable
import javax.inject.Inject

class OssFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentOssBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentOssBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val model = ViewModelProviders.of(this, viewModelFactory).get(OssViewModel::class.java)

        val adapter = OssRecyclerViewAdapter(clickCallback = {
            Log.d("Tag", "dependency = " + it.dependency)
            OssDialogFragment().show(childFragmentManager, "aleaht")
        })
        binding.recView.adapter = adapter

        model.getThirdPartyMetadata().observe(this, Observer {
            adapter.submitList(it)
        })
    }
}
