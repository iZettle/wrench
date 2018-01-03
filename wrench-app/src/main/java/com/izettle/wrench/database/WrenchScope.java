package com.izettle.wrench.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.izettle.wrench.database.tables.ApplicationTable;
import com.izettle.wrench.database.tables.ScopeTable;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = ScopeTable.TABLE_NAME,
        indices = {@Index(value = {ScopeTable.COL_APP_ID, ScopeTable.COL_NAME}, unique = true)},
        foreignKeys = @ForeignKey(entity = WrenchApplication.class,
                parentColumns = ApplicationTable.COL_ID,
                childColumns = ScopeTable.COL_APP_ID,
                onDelete = CASCADE))
public class WrenchScope {

    public static final String SCOPE_DEFAULT = "wrench_default";
    public static final String SCOPE_USER = "Development scope";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ScopeTable.COL_ID)
    private long id;

    @ColumnInfo(name = ScopeTable.COL_APP_ID)
    private long applicationId;

    @NonNull
    @ColumnInfo(name = ScopeTable.COL_NAME)
    private String name = SCOPE_DEFAULT;

    @NonNull
    @ColumnInfo(name = ScopeTable.COL_SELECTED_TIMESTAMP)
    private Date timeStamp = new Date();

    public WrenchScope() {
    }

    @Ignore
    public WrenchScope(long id, long applicationId, @NonNull String name, @NonNull Date timeStamp) {
        this.id = id;
        this.applicationId = applicationId;
        this.name = name;
        this.timeStamp = timeStamp;
    }

    public static boolean isDefaultScope(WrenchScope scope) {
        return TextUtils.equals(WrenchScope.SCOPE_DEFAULT, scope.getName());
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

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(@NonNull Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
