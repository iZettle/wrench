package com.izettle.localconfig.application.library;


import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.text.TextUtils;

import com.izettle.localconfig.application.BuildConfig;


public class Application implements Parcelable {
    @Keep
    public static Parcelable.Creator CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<Application>() {
        @Override
        public Application createFromParcel(Parcel in, ClassLoader loader) {
            return new Application(in);
        }

        @Override
        public Application[] newArray(int size) {
            return new Application[size];
        }
    });
    public long _id;
    public String applicationId;
    public String applicationLabel;

    public Application() {
    }

    public Application(Parcel in) {
        _id = in.readLong();
        applicationId = in.readString();
        applicationLabel = in.readString();
    }

    public static ContentValues toContentValues(Application application) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ApplicationCursorParser.Columns.APPLICATION_ID, application.applicationId);
        contentValues.put(ApplicationCursorParser.Columns.APPLICATION_LABEL, application.applicationLabel);
        return contentValues;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeString(applicationId);
        parcel.writeString(applicationLabel);
    }

    public boolean isConfigApplication() {
        return TextUtils.equals(applicationId, BuildConfig.APPLICATION_ID);
    }
}
