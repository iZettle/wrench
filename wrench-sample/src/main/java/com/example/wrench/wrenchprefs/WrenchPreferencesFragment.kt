package com.example.wrench.wrenchprefs


import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wrench.databinding.FragmentWrenchPreferencesBinding
import com.example.wrench.di.Injectable
import javax.inject.Inject

class WrenchPreferencesFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: WrenchPreferencesFragmentViewModel

    private lateinit var binding: FragmentWrenchPreferencesBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WrenchPreferencesFragmentViewModel::class.java)

        binding = FragmentWrenchPreferencesBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        return binding.root
    }
}
