package com.izettle.wrench.oss.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.izettle.wrench.databinding.FragmentOssBinding
import com.izettle.wrench.oss.detail.OssDetailFragment
import com.izettle.wrench.oss.detail.OssDetailFragmentArgs
import org.koin.androidx.viewmodel.ext.viewModel

class OssFragment : Fragment() {
    private lateinit var binding: FragmentOssBinding
    private val model: OssListViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentOssBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = OssRecyclerViewAdapter(clickCallback = {
            Log.d("Tag", "dependency = " + it.dependency)
            val ossDetailFragmentArgs = OssDetailFragmentArgs.Builder(it.dependency, it.skipBytes.toInt(), it.length).build()
            OssDetailFragment.newInstance(ossDetailFragmentArgs).show(childFragmentManager, "OssDetails")
        })
        binding.recView.adapter = adapter

        model.getThirdPartyMetadata().observe(this, Observer {
            adapter.submitList(it)
        })
    }
}
