package com.example.wrench;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.wrench.BoltLiveData.BoltLiveData;
import com.izettle.wrench.core.Bolt;

import javax.inject.Inject;

public class WrenchSampleViewModel extends AndroidViewModel {
    @Inject
    public WrenchSampleViewModel(@NonNull Application application) {
        super(application);
    }

    LiveData<Bolt> getStringBolt() {
        return BoltLiveData.create(getApplication(), getApplication().getResources().getString(R.string.string_configuration), "string1");
    }

    LiveData<Bolt> getIntBolt() {
        return BoltLiveData.create(getApplication(), getApplication().getResources().getString(R.string.int_configuration), 1);
    }

    LiveData<Bolt> getBooleanBolt() {
        return BoltLiveData.create(getApplication(), getApplication().getResources().getString(R.string.boolean_configuration), true);
    }

    LiveData<Bolt> getUrlBolt() {
        return BoltLiveData.create(getApplication(), getApplication().getResources().getString(R.string.url_configuration), "http://www.example.com/path?param=value");
    }

    LiveData<Bolt> getEnumBolt() {
        return BoltLiveData.create(getApplication(), getApplication().getResources().getString(R.string.enum_configuration), MainActivity.MyEnum.class, MainActivity.MyEnum.FIRST);
    }

    LiveData<Bolt> getServiceStringBolt() {
        return BoltLiveData.create(getApplication(), getApplication().getResources().getString(R.string.service_configuration), null);
    }
}
