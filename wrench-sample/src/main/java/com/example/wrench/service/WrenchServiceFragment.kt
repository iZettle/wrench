package com.example.wrench.service


import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wrench.databinding.FragmentWrenchPreferencesBinding
import com.example.wrench.databinding.FragmentWrenchServiceBinding
import com.example.wrench.di.Injectable
import javax.inject.Inject

class WrenchServiceFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: WrenchServiceFragmentViewModel

    private lateinit var binding: FragmentWrenchServiceBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WrenchServiceFragmentViewModel::class.java)

        binding = FragmentWrenchServiceBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        return binding.root
    }
}
