package com.example.wrench.wrenchprefs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wrench.databinding.FragmentWrenchPreferencesBinding
import org.koin.androidx.viewmodel.ext.viewModel

class WrenchPreferencesFragment : Fragment() {

    private val viewModel: WrenchPreferencesFragmentViewModel by viewModel()

    private lateinit var binding: FragmentWrenchPreferencesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWrenchPreferencesBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)

        binding.viewModel = viewModel
        return binding.root
    }
}
