package com.izettle.wrench.dialogs.enumvalue;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.izettle.wrench.database.WrenchConfiguration;
import com.izettle.wrench.database.WrenchConfigurationValue;
import com.izettle.wrench.database.WrenchDatabase;
import com.izettle.wrench.database.WrenchPredefinedConfigurationValue;

import java.util.List;

import javax.inject.Inject;

public class FragmentEnumValueViewModel extends ViewModel {

    private final WrenchDatabase wrenchDatabase;
    private LiveData<WrenchConfiguration> configuration;
    private long configurationId;
    private long scopeId;
    private LiveData<WrenchConfigurationValue> selectedConfigurationValueLiveData;
    private WrenchConfigurationValue selectedConfigurationValue;
    private LiveData<List<WrenchPredefinedConfigurationValue>> predefinedValuesLiveData;

    @SuppressWarnings("WeakerAccess")
    @Inject
    public FragmentEnumValueViewModel(WrenchDatabase wrenchDatabase) {
        this.wrenchDatabase = wrenchDatabase;
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

    LiveData<List<WrenchPredefinedConfigurationValue>> getPredefinedValues() {
        if (predefinedValuesLiveData == null) {
            predefinedValuesLiveData = wrenchDatabase.predefinedConfigurationValueDao().getByConfigurationId(configurationId);
        }
        return predefinedValuesLiveData;
    }

    WrenchConfigurationValue getSelectedConfigurationValue() {
        return selectedConfigurationValue;
    }

    void setSelectedConfigurationValue(WrenchConfigurationValue selectedConfigurationValue) {
        this.selectedConfigurationValue = selectedConfigurationValue;
    }
}
