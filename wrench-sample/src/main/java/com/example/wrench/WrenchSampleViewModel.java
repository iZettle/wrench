package com.example.wrench;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class WrenchSampleViewModel extends AndroidViewModel {
    @Inject
    public WrenchSampleViewModel(@NonNull Application application) {
        super(application);
    }
}
