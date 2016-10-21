package se.eelde.localconfig;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import se.eelde.localconfig.databinding.FragmentAddConfigurationBinding;
import se.eelde.localconfiguration.library.ConfigProviderHelper;
import se.eelde.localconfiguration.library.Configuration;


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
        typeAdapter.add("string");
        typeAdapter.add("boolean");
        typeAdapter.add("int");

        fragmentAddConfigurationBinding.typeSpinner.setAdapter(typeAdapter);

        fragmentAddConfigurationBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configuration configuration = new Configuration();
                configuration.key = fragmentAddConfigurationBinding.keyEditText.getText().toString();
                configuration.type = typeAdapter.getItem(fragmentAddConfigurationBinding.typeSpinner.getSelectedItemPosition());
                configuration.value = fragmentAddConfigurationBinding.valueEditText.getText().toString();

                getContext().getContentResolver().insert(ConfigProviderHelper.configurationUri(), Configuration.toContentValues(configuration));

                getActivity().finish();
            }
        });

        return fragmentAddConfigurationBinding.getRoot();
    }

}
