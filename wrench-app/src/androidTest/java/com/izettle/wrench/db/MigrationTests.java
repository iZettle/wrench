package com.izettle.wrench.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.room.testing.MigrationTestHelper;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.database.WrenchDatabase;
import com.izettle.wrench.database.tables.ConfigurationTable;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static com.izettle.wrench.database.migrations.Migrations.MIGRATION_1_2;
import static com.izettle.wrench.database.migrations.Migrations.MIGRATION_2_3;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MigrationTests {
    private static final String TEST_DB_NAME = "test_db";
    @Rule
    public MigrationTestHelper testHelper =
            new MigrationTestHelper(
                    InstrumentationRegistry.getInstrumentation(),
                    WrenchDatabase.class.getCanonicalName(),
                    new FrameworkSQLiteOpenHelperFactory());

    @Test
    public void test1to2() throws IOException {
        // Create the database with version 2
        SupportSQLiteDatabase originalDb = testHelper.createDatabase(TEST_DB_NAME, 1);

        // insert data

        originalDb.close();

        testHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MIGRATION_1_2);

    }

    @Test
    public void test2to3() throws IOException {
        // Create the database with version 2
        SupportSQLiteDatabase originalDb = testHelper.createDatabase(TEST_DB_NAME, 2);

        long testApplicationId = DatabaseHelper.insertWrenchApplication(originalDb, "TestApplication", "com.izettle.wrench.testapplication");


        // insert data
        DatabaseHelper.insertWrenchConfiguration(originalDb, testApplicationId, "Integerkey", Integer.class.getName());
        DatabaseHelper.insertWrenchConfiguration(originalDb, testApplicationId, "Stringkey", String.class.getName());
        DatabaseHelper.insertWrenchConfiguration(originalDb, testApplicationId, "Booleankey", Boolean.class.getName());
        DatabaseHelper.insertWrenchConfiguration(originalDb, testApplicationId, "Enumkey", Enum.class.getName());

        originalDb.close();

        SupportSQLiteDatabase migratedDb = testHelper.runMigrationsAndValidate(TEST_DB_NAME, 3, true, MIGRATION_2_3);

        Cursor cursor = DatabaseHelper.getWrenchConfigurationByKey(migratedDb, "Integerkey");
        assertTrue(cursor.moveToFirst());
        assertEquals(Bolt.TYPE.INTEGER, cursor.getString(cursor.getColumnIndex(ConfigurationTable.COL_TYPE)));
        cursor.close();

        cursor = DatabaseHelper.getWrenchConfigurationByKey(migratedDb, "Stringkey");
        assertTrue(cursor.moveToFirst());
        assertEquals(Bolt.TYPE.STRING, cursor.getString(cursor.getColumnIndex(ConfigurationTable.COL_TYPE)));
        cursor.close();

        cursor = DatabaseHelper.getWrenchConfigurationByKey(migratedDb, "Booleankey");
        assertTrue(cursor.moveToFirst());
        assertEquals(Bolt.TYPE.BOOLEAN, cursor.getString(cursor.getColumnIndex(ConfigurationTable.COL_TYPE)));
        cursor.close();

        cursor = DatabaseHelper.getWrenchConfigurationByKey(migratedDb, "Enumkey");
        assertTrue(cursor.moveToFirst());
        assertEquals(Bolt.TYPE.ENUM, cursor.getString(cursor.getColumnIndex(ConfigurationTable.COL_TYPE)));
        cursor.close();
    }

}
