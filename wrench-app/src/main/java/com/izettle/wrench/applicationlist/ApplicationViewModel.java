package com.izettle.wrench.applicationlist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.izettle.wrench.database.WrenchApplication;
import com.izettle.wrench.database.WrenchDatabase;

import java.util.List;

import javax.inject.Inject;

public class ApplicationViewModel extends ViewModel {

    private final LiveData<List<WrenchApplication>> applications;

    @Inject
    ApplicationViewModel(WrenchDatabase wrenchDatabase) {

        applications = wrenchDatabase.applicationDao().getApplications();
    }

    LiveData<List<WrenchApplication>> getApplications() {
        return applications;
    }
}
