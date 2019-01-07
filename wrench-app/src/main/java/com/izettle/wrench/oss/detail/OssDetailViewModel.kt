package com.izettle.wrench.oss.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.izettle.wrench.oss.LicenceMetadata

class OssDetailViewModel constructor(val context: Context) : ViewModel() {

    fun getThirdPartyMetadata(licenceMetadata: LicenceMetadata): LiveData<String> {
        return LicenceMetadataLiveData(context, licenceMetadata)
    }

}
