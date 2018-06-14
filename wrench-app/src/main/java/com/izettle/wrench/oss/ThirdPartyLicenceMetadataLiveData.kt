package com.izettle.wrench.oss

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

class ThirdPartyLicenceMetadataLiveData(val application: Application) : LiveData<List<LicenceMetadata>>() {

    init {
        AsyncTask.execute({
            postValue(OssLoading.getThirdPartyLicenceMetadata(application))
        })
    }
}
