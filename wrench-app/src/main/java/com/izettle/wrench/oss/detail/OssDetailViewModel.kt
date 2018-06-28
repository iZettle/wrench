package com.izettle.wrench.oss.detail

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.izettle.wrench.oss.LicenceMetadata
import javax.inject.Inject

class OssDetailViewModel @Inject constructor(val application: Application) : ViewModel() {

    fun getThirdPartyMetadata(licenceMetadata: LicenceMetadata): LiveData<String> {
        return LicenceMetadataLiveData(application, licenceMetadata)
    }

}
