package com.izettle.wrench.database.migrations;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;

public class Migrations {
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            {
                String tableName = "application";
                String tableNameTemp = tableName + "_temp";

                database.execSQL("CREATE TABLE `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageName` TEXT, `applicationLabel` TEXT)");
                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT * FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);
                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
            {
                String tableName = "configuration";
                String tableNameTemp = tableName + "_temp";

                database.execSQL("CREATE TABLE `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `applicationId` INTEGER NOT NULL, `configurationKey` TEXT, `configurationType` TEXT, FOREIGN KEY(`applicationId`) REFERENCES `application`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT * FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);
                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
            {
                String tableName = "configurationValue";
                String tableNameTemp = tableName + "_temp";

                database.execSQL("CREATE TABLE `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `configurationId` INTEGER NOT NULL, `value` TEXT, `scope` INTEGER NOT NULL, FOREIGN KEY(`configurationId`) REFERENCES `configuration`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT * FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);
                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
            {
                String tableName = "predefinedConfigurationValue";
                String tableNameTemp = tableName + "_temp";

                database.execSQL("CREATE TABLE `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `configurationId` INTEGER NOT NULL, `value` TEXT, FOREIGN KEY(`configurationId`) REFERENCES `configuration`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT * FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);
                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
            {
                String tableName = "scope";
                String tableNameTemp = tableName + "_temp";

                database.execSQL("CREATE TABLE `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `applicationId` INTEGER NOT NULL, `name` TEXT, `selectedTimestamp` INTEGER, FOREIGN KEY(`applicationId`) REFERENCES `application`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT * FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);
                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
        }
    };
}
