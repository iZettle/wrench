package com.izettle.wrench.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.izettle.wrench.database.tables.ApplicationTable;
import com.izettle.wrench.database.tables.ConfigurationTable;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = ConfigurationTable.TABLE_NAME,
        indices = {@Index(value = {ConfigurationTable.COL_APP_ID, ConfigurationTable.COL_KEY}, unique = true)},
        foreignKeys = @ForeignKey(entity = WrenchApplication.class,
                parentColumns = ApplicationTable.COL_ID,
                childColumns = ConfigurationTable.COL_APP_ID,
                onDelete = CASCADE))
public class WrenchConfiguration {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ConfigurationTable.COL_ID)
    private long id;

    @ColumnInfo(name = ConfigurationTable.COL_APP_ID)
    private long applicationId;

    @ColumnInfo(name = ConfigurationTable.COL_KEY)
    private String key;

    @NonNull
    @ColumnInfo(name = ConfigurationTable.COL_TYPE)
    private String type;

    @NonNull
    private Date lastUse = new Date();

    public WrenchConfiguration(long id, long applicationId, String key, @NonNull String type) {
        this.id = id;
        this.applicationId = applicationId;
        this.key = key;
        this.type = type;
        this.lastUse = new Date();
    }

    public long id() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long applicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String key() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String type() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Nullable
    public Date getLastUse() {
        return lastUse;
    }

    public void setLastUse(@Nullable Date lastUse) {
        this.lastUse = lastUse;
    }
}
