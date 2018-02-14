package com.example.wrench.livedataprefs


import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wrench.R
import com.example.wrench.databinding.FragmentLiveDataPreferencesBinding
import com.example.wrench.di.Injectable
import com.izettle.wrench.service.WrenchService
import java.util.*
import javax.inject.Inject

class LiveDataPreferencesFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        @JvmStatic
        fun newInstance(): LiveDataPreferencesFragment {
            return LiveDataPreferencesFragment()
        }
    }

    private lateinit var viewModel: LiveDataPreferencesFragmentViewModel

    private lateinit var binding: FragmentLiveDataPreferencesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LiveDataPreferencesFragmentViewModel::class.java)

        binding = FragmentLiveDataPreferencesBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.serviceButton.setOnClickListener({ v ->
            val intent = Intent(v.context, WrenchService::class.java)
            intent.putExtra(getString(R.string.service_configuration), Date().toString())
            context?.startService(intent)
        })
    }
}

