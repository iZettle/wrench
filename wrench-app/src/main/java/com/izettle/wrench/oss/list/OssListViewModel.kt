package com.izettle.wrench.oss.list

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.izettle.wrench.oss.LicenceMetadata
import javax.inject.Inject

class OssListViewModel @Inject constructor(val application: Application) : ViewModel() {
    fun getThirdPartyMetadata(): LiveData<List<LicenceMetadata>> {
        return ThirdPartyLicenceMetadataLiveData(application)
    }
}
