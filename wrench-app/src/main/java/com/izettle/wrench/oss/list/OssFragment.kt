package com.izettle.wrench.oss.list


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.izettle.wrench.databinding.FragmentOssBinding
import com.izettle.wrench.di.Injectable
import com.izettle.wrench.oss.detail.OssDetailFragment
import com.izettle.wrench.oss.detail.OssDetailFragmentArgs
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
        val model = ViewModelProviders.of(this, viewModelFactory).get(OssListViewModel::class.java)

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
