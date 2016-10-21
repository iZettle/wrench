package se.eelde.localconfiguration.library;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

public class Configuration implements Parcelable {
    public static final String[] PROJECTION = new String[]{
            Columns._ID,
            Columns.APPLICATION_ID,
            Columns.KEY,
            Columns.VALUE,
            Columns.TYPE
    };
    public static final Creator<Configuration> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<Configuration>() {
        @Override
        public Configuration createFromParcel(Parcel in, ClassLoader loader) {
            return new Configuration(in);
        }

        @Override
        public Configuration[] newArray(int size) {
            return new Configuration[size];
        }
    });
    public long _id;
    public long applicationId;
    public String type;
    public String key;
    public String value;

    public Configuration() {
    }

    @SuppressWarnings("WeakerAccess")
    public Configuration(Parcel parcel) {
        _id = parcel.readLong();
        applicationId = parcel.readLong();
        type = parcel.readString();
        key = parcel.readString();
        value = parcel.readString();
    }

    public static Configuration configurationFromCursor(Cursor cursor) {
        Configuration configuration = new Configuration();

        configuration._id = cursor.getLong(cursor.getColumnIndex(Columns._ID));
        configuration.applicationId = cursor.getLong(cursor.getColumnIndex(Columns.APPLICATION_ID));
        configuration.key = cursor.getString(cursor.getColumnIndex(Columns.KEY));
        configuration.value = cursor.getString(cursor.getColumnIndex(Columns.VALUE));
        configuration.type = cursor.getString(cursor.getColumnIndex(Columns.TYPE));

        return configuration;
    }

    public static ContentValues toContentValues(Configuration configuration) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.APPLICATION_ID, configuration.applicationId);
        contentValues.put(Columns.KEY, configuration.key);
        contentValues.put(Columns.VALUE, configuration.value);
        contentValues.put(Columns.TYPE, configuration.type);
        return contentValues;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeLong(applicationId);
        parcel.writeString(type);
        parcel.writeString(key);
        parcel.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public interface Columns extends BaseColumns {
        String APPLICATION_ID = "applicationName";
        String KEY = "key";
        String VALUE = "value";
        String TYPE = "type";
    }
}
