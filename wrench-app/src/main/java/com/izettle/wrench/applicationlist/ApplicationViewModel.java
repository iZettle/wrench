package com.izettle.wrench.applicationlist;


import com.izettle.wrench.database.WrenchApplication;
import com.izettle.wrench.database.WrenchApplicationDao;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class ApplicationViewModel extends ViewModel {

    private final MediatorLiveData<PagedList<WrenchApplication>> mediatedApplications;

    private final MutableLiveData<Boolean> listEmpty;

    @Inject
    ApplicationViewModel(WrenchApplicationDao applicationDao) {

        listEmpty = new MutableLiveData<>();
        listEmpty.setValue(true);

        LiveData<PagedList<WrenchApplication>> applications =
                new LivePagedListBuilder<>(applicationDao.getApplications(),
                        new PagedList.Config.Builder()
                                .setEnablePlaceholders(true)
                                .setPageSize(10)
                                .setPrefetchDistance(10)
                                .build()).build();

        mediatedApplications = new MediatorLiveData<>();

        mediatedApplications.addSource(applications, wrenchApplications -> {
            listEmpty.setValue(wrenchApplications == null || wrenchApplications.size() == 0);
            mediatedApplications.setValue(wrenchApplications);
        });
    }

    LiveData<PagedList<WrenchApplication>> getApplications() {
        return mediatedApplications;
    }

    LiveData<Boolean> isListEmpty() {
        return listEmpty;
    }
}
