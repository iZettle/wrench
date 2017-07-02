package com.izettle.wrench;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.izettle.wrench.database.WrenchApplication;
import com.izettle.wrench.database.WrenchDatabase;

import java.util.List;

public class ApplicationViewModel extends AndroidViewModel {

    private final LiveData<List<WrenchApplication>> applications;

    public ApplicationViewModel(Application application) {
        super(application);

        WrenchDatabase wrenchDatabase = WrenchDatabase.getDatabase(application);

        applications = wrenchDatabase.applicationDao().getApplications();
    }

    LiveData<List<WrenchApplication>> getApplications() {
        return applications;
    }
}
