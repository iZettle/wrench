package com.izettle.localconfig.application.library;


import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;


public class Application implements Parcelable {
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
    public String applicationName;
    public String label;

    public Application() {
    }

    public Application(Parcel in) {
        _id = in.readLong();
        applicationName = in.readString();
        label = in.readString();
    }

    public static ContentValues toContentValues(Application application) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ApplicationCursorParser.Columns.APPLICATION_NAME, application.applicationName);
        contentValues.put(ApplicationCursorParser.Columns.LABEL, application.label);
        return contentValues;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeString(applicationName);
        parcel.writeString(label);
    }
}
