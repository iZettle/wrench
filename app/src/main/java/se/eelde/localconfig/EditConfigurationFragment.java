package se.eelde.localconfig;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.eelde.localconfiguration.library.Configuration;

public class EditConfigurationFragment extends Fragment {
    private static final String ARG_CONFIGURATION = "ARG_CONFIGURATION";

    public EditConfigurationFragment() {
    }

    public static EditConfigurationFragment newInstance(Configuration configuration) {
        EditConfigurationFragment fragment = new EditConfigurationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONFIGURATION, configuration);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_configuration, container, false);
    }

}
