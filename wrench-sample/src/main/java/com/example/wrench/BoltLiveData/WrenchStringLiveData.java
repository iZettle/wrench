package com.example.wrench.BoltLiveData;

import android.content.Context;
import android.net.Uri;

import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.core.WrenchProviderContract;

class WrenchStringLiveData extends WrenchLiveData<String> {
    private final String defValue;

    WrenchStringLiveData(Context context, String key, String defValue, String type) {
        super(context, key, type);

        this.defValue = defValue;
    }

    @Override
    void boltChanged(Bolt bolt) {
        if (bolt == null) {
            setValue(defValue);
            return;
        }

        if (bolt.getId() == 0) {
            bolt = bolt.copy(bolt.getId(), getType(), getKey(), defValue);
            Uri uri = getContext().getContentResolver().insert(WrenchProviderContract.boltUri(), bolt.toContentValues());
            bolt.setId(Long.parseLong(uri.getLastPathSegment()));
        }
        setValue(bolt.getValue());
    }
}
