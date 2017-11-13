package com.example.wrench;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.izettle.wrench.core.Bolt;

public class WrenchSampleViewModel extends AndroidViewModel {
    public WrenchSampleViewModel(@NonNull Application application) {
        super(application);
    }

    LiveData<Bolt> getStringBolt() {
        return BoltLiveData.string(getApplication(), getApplication().getResources().getString(R.string.string_configuration), "string1");
    }

    LiveData<Bolt> getIntBolt() {
        return BoltLiveData.integer(getApplication(), getApplication().getResources().getString(R.string.int_configuration), 1);
    }

    LiveData<Bolt> getBooleanBolt() {
        return BoltLiveData.bool(getApplication(), getApplication().getResources().getString(R.string.boolean_configuration), true);
    }

    LiveData<Bolt> getUrlBolt() {
        return BoltLiveData.string(getApplication(), getApplication().getResources().getString(R.string.url_configuration), "http://www.example.com/path?param=value");
    }

    LiveData<Bolt> getBolt(String key) {
        return new BoltLiveData(getApplication(), key, null, null);
    }

}
