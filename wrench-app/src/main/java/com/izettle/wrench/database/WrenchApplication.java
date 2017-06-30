package com.izettle.wrench.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.izettle.wrench.database.tables.ApplicationTable;

@Entity(tableName = ApplicationTable.TABLE_NAME,
        indices = {@Index(value = {ApplicationTable.COL_PACK_NAME}, unique = true)})
public class WrenchApplication {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ApplicationTable.COL_ID)
    private long id;

    @ColumnInfo(name = ApplicationTable.COL_PACK_NAME)
    private String packageName;

    @ColumnInfo(name = ApplicationTable.COL_APP_LABEL)
    private String applicationLabel;

    public void setId(long id) {
        this.id = id;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setApplicationLabel(String applicationLabel) {
        this.applicationLabel = applicationLabel;
    }

    public long id() {
        return id;
    }

    public String packageName() {
        return packageName;
    }

    public String applicationLabel() {
        return applicationLabel;
    }
}
