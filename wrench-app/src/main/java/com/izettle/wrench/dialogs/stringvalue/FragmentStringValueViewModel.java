package com.izettle.wrench.dialogs.stringvalue;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.izettle.wrench.database.WrenchConfiguration;
import com.izettle.wrench.database.WrenchConfigurationValue;
import com.izettle.wrench.database.WrenchDatabase;
import com.izettle.wrench.database.WrenchScope;

public class FragmentStringValueViewModel extends AndroidViewModel {

    private final WrenchDatabase wrenchDatabase;
    private LiveData<WrenchConfiguration> configuration;
    private LiveData<WrenchScope> scope;
    private long configurationId;
    private long scopeId;
    private LiveData<WrenchConfigurationValue> selectedConfigurationValueLiveData;
    private WrenchConfigurationValue selectedConfigurationValue;

    FragmentStringValueViewModel(Application application) {
        super(application);

        wrenchDatabase = WrenchDatabase.getDatabase(application);
    }

    void init(long configurationId, long scopeId) {
        this.configurationId = configurationId;
        this.scopeId = scopeId;
    }

    LiveData<WrenchConfiguration> getConfiguration() {
        if (configuration == null) {
            configuration = wrenchDatabase.configurationDao().getConfiguration(configurationId);
        }
        return configuration;
    }

    LiveData<WrenchScope> getScope() {
        if (scope == null) {
            scope = wrenchDatabase.scopeDao().getScope(scopeId);
        }
        return scope;
    }

    public void updateConfigurationValue(String value) {
        if (selectedConfigurationValue != null) {
            wrenchDatabase.configurationValueDao().updateConfigurationValue(configurationId, scopeId, value);
        } else {
            WrenchConfigurationValue wrenchConfigurationValue = new WrenchConfigurationValue();
            wrenchConfigurationValue.setConfigurationId(configurationId);
            wrenchConfigurationValue.setScope(scopeId);
            wrenchConfigurationValue.setValue(value);
            wrenchConfigurationValue.setId(wrenchDatabase.configurationValueDao().insert(wrenchConfigurationValue));
        }
    }

    void deleteConfigurationValue() {
        wrenchDatabase.configurationValueDao().delete(selectedConfigurationValue);
    }

    LiveData<WrenchConfigurationValue> getSelectedConfigurationValueLiveData() {
        if (selectedConfigurationValueLiveData == null) {
            selectedConfigurationValueLiveData = wrenchDatabase.configurationValueDao().getConfigurationValue(configurationId, scopeId);
        }
        return selectedConfigurationValueLiveData;
    }

    public WrenchConfigurationValue getSelectedConfigurationValue() {
        return selectedConfigurationValue;
    }

    void setSelectedConfigurationValue(WrenchConfigurationValue selectedConfigurationValue) {
        this.selectedConfigurationValue = selectedConfigurationValue;
    }
}
