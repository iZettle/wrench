package se.izettle.localconfig;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.izettle.localconfig.application.databinding.FragmentAddConfigurationBinding;

import se.izettle.localconfiguration.library.ConfigProviderHelper;
import se.izettle.localconfiguration.library.Configuration;
import se.izettle.localconfiguration.library.util.ConfigurationContentValueProducer;


public class AddConfigurationFragment extends Fragment {

    private FragmentAddConfigurationBinding fragmentAddConfigurationBinding;
    private ArrayAdapter<String> typeAdapter;

    public AddConfigurationFragment() {
    }

    public static AddConfigurationFragment newInstance() {
        AddConfigurationFragment fragment = new AddConfigurationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentAddConfigurationBinding = FragmentAddConfigurationBinding.inflate(inflater, container, false);

        typeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        typeAdapter.add(String.class.getName());
        typeAdapter.add(Boolean.class.getName());
        typeAdapter.add(Integer.class.getName());

        fragmentAddConfigurationBinding.typeSpinner.setAdapter(typeAdapter);

        fragmentAddConfigurationBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configuration configuration = new Configuration();
                configuration.key = fragmentAddConfigurationBinding.keyEditText.getText().toString();
                configuration.type = typeAdapter.getItem(fragmentAddConfigurationBinding.typeSpinner.getSelectedItemPosition());
                configuration.value = fragmentAddConfigurationBinding.valueEditText.getText().toString();

                getContext().getContentResolver().insert(ConfigProviderHelper.configurationUri(), new ConfigurationContentValueProducer().toContentValues(configuration));

                getActivity().finish();
            }
        });

        return fragmentAddConfigurationBinding.getRoot();
    }

}
