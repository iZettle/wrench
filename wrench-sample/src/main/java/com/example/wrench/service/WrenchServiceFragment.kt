package com.example.wrench.service


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wrench.databinding.FragmentWrenchServiceBinding
import org.koin.androidx.viewmodel.ext.viewModel


class WrenchServiceFragment : Fragment() {

    private val viewModel: WrenchServiceFragmentViewModel by viewModel()

    private lateinit var binding: FragmentWrenchServiceBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWrenchServiceBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        return binding.root
    }
}
