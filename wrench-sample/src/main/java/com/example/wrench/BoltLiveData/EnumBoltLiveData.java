package com.example.wrench.BoltLiveData;

import android.content.Context;
import android.net.Uri;

import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.core.Nut;
import com.izettle.wrench.core.WrenchProviderContract;

class EnumBoltLiveData<T extends Enum> extends BoltLiveData {
    private final Class<T> enumClass;
    private final T defValue;

    EnumBoltLiveData(Context context, String key, Class<T> enumClass, T defValue) {
        super(context, key, Bolt.TYPE.ENUM);
        this.enumClass = enumClass;
        this.defValue = defValue;
    }

    private Uri insertBolt(Bolt bolt) {
        return getContext().getContentResolver().insert(WrenchProviderContract.boltUri(), bolt.toContentValues());
    }

    private void insertNut(Nut nut) {
        getContext().getContentResolver().insert(WrenchProviderContract.nutUri(), nut.toContentValues());
    }

    @Override
    void boltChanged(Bolt bolt) {
        if (bolt == null) {
            setValue(new Bolt(0, getType(), getKey(), String.valueOf(defValue)));
            return;
        }

        if (bolt.getId() == 0) {
            bolt = bolt.copy(bolt.getId(), getType(), getKey(), String.valueOf(defValue));
            Uri uri = insertBolt(bolt);
            bolt.setId(Long.parseLong(uri.getLastPathSegment()));

            for (T t : enumClass.getEnumConstants()) {
                insertNut(new Nut(bolt.getId(), t.toString()));
            }
        }
        setValue(bolt);
    }
}
