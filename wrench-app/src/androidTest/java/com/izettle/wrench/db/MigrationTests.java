package com.izettle.wrench.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.room.testing.MigrationTestHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.izettle.wrench.database.WrenchDatabase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static com.izettle.wrench.database.migrations.Migrations.MIGRATION_1_2;
import static com.izettle.wrench.database.migrations.Migrations.MIGRATION_2_3;

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
        SupportSQLiteDatabase db = testHelper.createDatabase(TEST_DB_NAME, 1);

        // insert data

        db.close();

        testHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MIGRATION_1_2);

    }

    @Test
    public void test1to3() throws IOException {
        // Create the database with version 2
        SupportSQLiteDatabase db = testHelper.createDatabase(TEST_DB_NAME, 1);

        // insert data

        db.close();

        testHelper.runMigrationsAndValidate(TEST_DB_NAME, 3, true, MIGRATION_1_2, MIGRATION_2_3);

    }

    @Test
    public void test2to3() throws IOException {
        // Create the database with version 2
        SupportSQLiteDatabase db = testHelper.createDatabase(TEST_DB_NAME, 2);

        // insert data

        db.close();

        testHelper.runMigrationsAndValidate(TEST_DB_NAME, 3, true, MIGRATION_2_3);

    }


}
