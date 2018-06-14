package com.izettle.wrench.oss

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class OssViewModel @Inject constructor(val application: Application) : ViewModel() {
    fun getThirdPartyMetadata(): LiveData<List<LicenceMetadata>> {
        return ThirdPartyLicenceMetadataLiveData(application)
    }
}
