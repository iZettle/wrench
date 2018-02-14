package com.example.wrench.BoltLiveData;

import android.content.Context;
import android.net.Uri;

import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.core.WrenchProviderContract;

class WrenchIntLiveData extends WrenchLiveData<Integer> {
    private final int defValue;

    WrenchIntLiveData(Context context, String key, int defValue) {
        super(context, key, Bolt.TYPE.INTEGER);

        this.defValue = defValue;
    }

    @Override
    void boltChanged(Bolt bolt) {
        if (bolt == null) {
            setValue(defValue);
            return;
        }

        if (bolt.getId() == 0) {
            bolt = bolt.copy(bolt.getId(), getType(), getKey(), String.valueOf(defValue));
            Uri uri = getContext().getContentResolver().insert(WrenchProviderContract.boltUri(), bolt.toContentValues());
            bolt.setId(Long.parseLong(uri.getLastPathSegment()));
        }
        setValue(Integer.valueOf(bolt.getValue()));
    }
}
