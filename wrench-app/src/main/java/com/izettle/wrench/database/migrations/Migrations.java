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

                // create new table with temp name and temp index
                database.execSQL("CREATE TABLE `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageName` TEXT, `applicationLabel` TEXT)");
                database.execSQL("CREATE UNIQUE INDEX `index_application_temp_packageName` ON `" + tableNameTemp + "` (`packageName`)");

                // copy data from old table + drop it
                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT * FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);

                // recreate index with correct name
                database.execSQL("DROP INDEX `index_application_temp_packageName`");
                database.execSQL("CREATE UNIQUE INDEX `index_application_packageName` ON `" + tableNameTemp + "` (`packageName`)");

                // rename database
                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
            {
                String tableName = "configuration";
                String tableNameTemp = tableName + "_temp";

                database.execSQL("CREATE TABLE `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `applicationId` INTEGER NOT NULL, `configurationKey` TEXT, `configurationType` TEXT, FOREIGN KEY(`applicationId`) REFERENCES `application`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
                database.execSQL("CREATE UNIQUE INDEX `index_configuration_temp_applicationId_configurationKey` ON `" + tableNameTemp + "` (`applicationId`, `configurationKey`)");

                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT * FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);

                database.execSQL("DROP INDEX `index_configuration_temp_applicationId_configurationKey`");
                database.execSQL("CREATE UNIQUE INDEX `index_configuration_applicationId_configurationKey` ON `" + tableNameTemp + "` (`applicationId`, `configurationKey`)");

                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
            {
                String tableName = "configurationValue";
                String tableNameTemp = tableName + "_temp";

                database.execSQL("CREATE TABLE `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `configurationId` INTEGER NOT NULL, `value` TEXT, `scope` INTEGER NOT NULL, FOREIGN KEY(`configurationId`) REFERENCES `configuration`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
                database.execSQL("CREATE UNIQUE INDEX `index_configurationValue_temp_configurationId_value_scope` ON `" + tableNameTemp + "` (`configurationId`, `value`, `scope`)");

                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT * FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);

                database.execSQL("DROP INDEX `index_configurationValue_temp_configurationId_value_scope`");
                database.execSQL("CREATE UNIQUE INDEX `index_configurationValue_configurationId_value_scope` ON `" + tableNameTemp + "` (`configurationId`, `value`, `scope`)");

                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
            {
                String tableName = "predefinedConfigurationValue";
                String tableNameTemp = tableName + "_temp";

                database.execSQL("CREATE TABLE `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `configurationId` INTEGER NOT NULL, `value` TEXT, FOREIGN KEY(`configurationId`) REFERENCES `configuration`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
                database.execSQL("CREATE  INDEX `index_predefinedConfigurationValue_temp_configurationId` ON `" + tableNameTemp + "` (`configurationId`)");

                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT * FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);

                database.execSQL("DROP INDEX `index_predefinedConfigurationValue_temp_configurationId`");
                database.execSQL("CREATE  INDEX `index_predefinedConfigurationValue_configurationId` ON `" + tableNameTemp + "` (`configurationId`)");

                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
            {
                String tableName = "scope";
                String tableNameTemp = tableName + "_temp";

                database.execSQL("CREATE TABLE `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `applicationId` INTEGER NOT NULL, `name` TEXT, `selectedTimestamp` INTEGER, FOREIGN KEY(`applicationId`) REFERENCES `application`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
                database.execSQL("CREATE UNIQUE INDEX `index_scope_temp_applicationId_name` ON `" + tableNameTemp + "` (`applicationId`, `name`)");

                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT * FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);

                database.execSQL("DROP INDEX `index_scope_temp_applicationId_name`");
                database.execSQL("CREATE UNIQUE INDEX `index_scope_applicationId_name` ON `" + tableNameTemp + "` (`applicationId`, `name`)");

                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            {
                String tableName = "application";
                String tableNameTemp = tableName + "_temp";

                database.execSQL("CREATE TABLE IF NOT EXISTS `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageName` TEXT NOT NULL, `applicationLabel` TEXT NOT NULL)");
                database.execSQL("CREATE UNIQUE INDEX `index_application_temp_packageName` ON `" + tableNameTemp + "` (`packageName`)");

                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT * FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);

                // recreate index with correct name
                database.execSQL("DROP INDEX `index_application_temp_packageName`");
                database.execSQL("CREATE UNIQUE INDEX `index_application_packageName` ON `" + tableNameTemp + "` (`packageName`)");

                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
            {
                String tableName = "configuration";
                String tableNameTemp = tableName + "_temp";

                database.execSQL("CREATE TABLE IF NOT EXISTS `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `applicationId` INTEGER NOT NULL, `configurationKey` TEXT, `configurationType` TEXT NOT NULL, `lastUse` INTEGER NOT NULL DEFAULT 0, FOREIGN KEY(`applicationId`) REFERENCES `application`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
                database.execSQL("CREATE UNIQUE INDEX `index_configuration_temp_applicationId_configurationKey` ON `" + tableNameTemp + "` (`applicationId`, `configurationKey`)");

                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT id, applicationId, configurationKey, configurationType, 0 FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);

                database.execSQL("DROP INDEX `index_configuration_temp_applicationId_configurationKey`");
                database.execSQL("CREATE UNIQUE INDEX `index_configuration_applicationId_configurationKey` ON `" + tableNameTemp + "` (`applicationId`, `configurationKey`)");

                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
            {
                String tableName = "scope";
                String tableNameTemp = tableName + "_temp";

                database.execSQL("CREATE TABLE IF NOT EXISTS `" + tableNameTemp + "` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `applicationId` INTEGER NOT NULL, `name` TEXT NOT NULL, `selectedTimestamp` INTEGER NOT NULL, FOREIGN KEY(`applicationId`) REFERENCES `application`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
                database.execSQL("CREATE UNIQUE INDEX `index_scope_temp_applicationId_name` ON `" + tableNameTemp + "` (`applicationId`, `name`)");

                database.execSQL("INSERT INTO " + tableNameTemp + " SELECT * FROM " + tableName);
                database.execSQL("DROP TABLE " + tableName);

                database.execSQL("DROP INDEX `index_scope_temp_applicationId_name`");
                database.execSQL("CREATE UNIQUE INDEX `index_scope_applicationId_name` ON `" + tableNameTemp + "` (`applicationId`, `name`)");

                database.execSQL("ALTER TABLE " + tableNameTemp + " RENAME TO " + tableName);
            }
        }
    };
}
