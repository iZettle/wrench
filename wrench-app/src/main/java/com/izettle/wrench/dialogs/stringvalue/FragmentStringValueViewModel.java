package com.izettle.wrench.dialogs.stringvalue;


import com.izettle.wrench.database.WrenchConfiguration;
import com.izettle.wrench.database.WrenchConfigurationDao;
import com.izettle.wrench.database.WrenchConfigurationValue;
import com.izettle.wrench.database.WrenchConfigurationValueDao;

import java.util.Date;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class FragmentStringValueViewModel extends ViewModel {

    private final WrenchConfigurationDao configurationDao;
    private final WrenchConfigurationValueDao configurationValueDao;
    private LiveData<WrenchConfiguration> configuration;
    private long configurationId;
    private long scopeId;
    private LiveData<WrenchConfigurationValue> selectedConfigurationValueLiveData;
    private WrenchConfigurationValue selectedConfigurationValue;

    @SuppressWarnings("WeakerAccess")
    @Inject
    public FragmentStringValueViewModel(WrenchConfigurationDao configurationDao, WrenchConfigurationValueDao configurationValueDao) {
        this.configurationDao = configurationDao;
        this.configurationValueDao = configurationValueDao;
    }

    void init(long configurationId, long scopeId) {
        this.configurationId = configurationId;
        this.scopeId = scopeId;
    }

    LiveData<WrenchConfiguration> getConfiguration() {
        if (configuration == null) {
            configuration = configurationDao.getConfiguration(configurationId);
        }
        return configuration;
    }

    public void updateConfigurationValue(String value) {
        if (selectedConfigurationValue != null) {
            configurationValueDao.updateConfigurationValue(configurationId, scopeId, value);
        } else {
            WrenchConfigurationValue wrenchConfigurationValue = new WrenchConfigurationValue(0, configurationId, value, scopeId);
            wrenchConfigurationValue.setId(configurationValueDao.insert(wrenchConfigurationValue));
        }
        configurationDao.touch(configurationId, new Date());
    }

    void deleteConfigurationValue() {
        configurationValueDao.delete(selectedConfigurationValue);
    }

    LiveData<WrenchConfigurationValue> getSelectedConfigurationValueLiveData() {
        if (selectedConfigurationValueLiveData == null) {
            selectedConfigurationValueLiveData = configurationValueDao.getConfigurationValue(configurationId, scopeId);
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
