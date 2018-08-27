package com.izettle.wrench.oss.list

import android.app.Application
import androidx.lifecycle.LiveData
import com.izettle.wrench.oss.LicenceMetadata
import kotlinx.coroutines.experimental.async

class ThirdPartyLicenceMetadataLiveData(val application: Application) : LiveData<List<LicenceMetadata>>() {

    init {
        async {
            postValue(OssLoading.getThirdPartyLicenceMetadata(application))
        }
    }
}
