package com.izettle.wrench.oss.detail

import android.app.Application
import androidx.lifecycle.LiveData
import com.izettle.wrench.oss.LicenceMetadata
import com.izettle.wrench.oss.list.OssLoading
import kotlinx.coroutines.experimental.async

class LicenceMetadataLiveData(val application: Application, val licenceMetadata: LicenceMetadata) : LiveData<String>() {

    init {
        run {
            postValue(OssLoading.getThirdPartyLicence(application, licenceMetadata))
        }
    }
}
