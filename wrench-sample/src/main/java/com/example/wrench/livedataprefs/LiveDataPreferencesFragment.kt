package com.example.wrench.livedataprefs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wrench.databinding.FragmentLiveDataPreferencesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LiveDataPreferencesFragment : Fragment() {

    private val viewModel: LiveDataPreferencesFragmentViewModel by viewModel()

    private lateinit var binding: FragmentLiveDataPreferencesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLiveDataPreferencesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}

