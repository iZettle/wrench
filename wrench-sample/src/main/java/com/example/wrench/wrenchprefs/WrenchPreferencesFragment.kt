package com.example.wrench.wrenchprefs


import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.wrench.MyEnum
import com.example.wrench.R
import com.example.wrench.databinding.FragmentWrenchPreferencesBinding
import com.example.wrench.di.Injectable
import com.izettle.wrench.preferences.WrenchPreferences
import javax.inject.Inject

class WrenchPreferencesFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var wrenchPreferences: WrenchPreferences

    private lateinit var viewModel: WrenchPreferencesFragmentViewModel

    private lateinit var binding: FragmentWrenchPreferencesBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WrenchPreferencesFragmentViewModel::class.java)

        binding = FragmentWrenchPreferencesBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)

        AsyncTask.execute {
            wrenchPreferences.getString(resources.getString(R.string.string_configuration), "string1")
        }

        AsyncTask.execute {
            wrenchPreferences.getString(resources.getString(R.string.url_configuration), "http://www.example.com/path?param=value")
        }

        AsyncTask.execute {
            wrenchPreferences.getBoolean(resources.getString(R.string.boolean_configuration), true)
        }

        AsyncTask.execute {
            wrenchPreferences.getInt(resources.getString(R.string.int_configuration), 1)
        }

        AsyncTask.execute {
            wrenchPreferences.getEnum(resources.getString(R.string.enum_configuration), MyEnum::class.java, MyEnum.SECOND)
        }

        AsyncTask.execute {
            wrenchPreferences.getString(resources.getString(R.string.service_configuration), null)
        }

        binding.viewModel = viewModel

        return binding.root
    }
}
