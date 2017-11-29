package com.izettle.wrench.provider;

import android.arch.persistence.room.Room;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.izettle.wrench.BuildConfig;
import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.core.Nut;
import com.izettle.wrench.core.WrenchProviderContract;
import com.izettle.wrench.database.WrenchDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WrenchProviderTest extends ProviderTestCase2<WrenchProvider> {

    private MockContentResolver contentResolver;

    public WrenchProviderTest() {
        super(WrenchProvider.class, BuildConfig.CONFIG_AUTHORITY);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        contentResolver = getMockContentResolver();

        WrenchDatabase wrenchDatabase = Room.inMemoryDatabaseBuilder(getMockContext(), WrenchDatabase.class).build();

        if (getProvider().applicationDao != null) {
            throw new IllegalStateException("applicationDao should be null here");
        }

        getProvider().applicationDao = wrenchDatabase.applicationDao();
        getProvider().configurationDao = wrenchDatabase.configurationDao();
        getProvider().configurationValueDao = wrenchDatabase.configurationValueDao();
        getProvider().scopeDao = wrenchDatabase.scopeDao();
        getProvider().predefinedConfigurationDao = wrenchDatabase.predefinedConfigurationValueDao();

        getProvider().packageManagerWrapper = new TestPackageManagerWrapper("TestApplication", "com.test.application");
    }

    @Test
    public void testInsertBolt() throws Exception {
        String insertBoltKey = "insertBoltKey";

        Uri uri = WrenchProviderContract.boltUri();
        Bolt insertBolt = getBolt(insertBoltKey);
        Uri insertBoltUri = contentResolver.insert(uri, insertBolt.toContentValues());
        assertNotNull(insertBoltUri);

        Cursor cursor = contentResolver.query(WrenchProviderContract.boltUri(insertBoltKey), null, null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());

        cursor.moveToFirst();
        Bolt queryBolt = Bolt.fromCursor(cursor);

        assertEquals(insertBolt.getKey(), queryBolt.getKey());
        assertEquals(insertBolt.getValue(), queryBolt.getValue());
        assertEquals(insertBolt.getType(), queryBolt.getType());

        cursor = contentResolver.query(WrenchProviderContract.boltUri(Integer.parseInt(insertBoltUri.getLastPathSegment())), null, null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());

        cursor.moveToFirst();
        queryBolt = Bolt.fromCursor(cursor);

        assertEquals(insertBolt.getKey(), queryBolt.getKey());
        assertEquals(insertBolt.getValue(), queryBolt.getValue());
        assertEquals(insertBolt.getType(), queryBolt.getType());
    }

    @Test
    public void testUpdateBolt() throws Exception {
        String updateBoltKey = "updateBoltKey";

        Uri uri = WrenchProviderContract.boltUri();
        Bolt insertBolt = getBolt(updateBoltKey);
        Uri insertBoltUri = contentResolver.insert(uri, insertBolt.toContentValues());
        assertNotNull(insertBoltUri);

        Cursor cursor = contentResolver.query(WrenchProviderContract.boltUri(updateBoltKey), null, null, null, null);
        assertNotNull(cursor);
        assertTrue(cursor.moveToFirst());

        Bolt providerBolt = Bolt.fromCursor(cursor);
        assertEquals(insertBolt.getKey(), providerBolt.getKey());
        assertEquals(insertBolt.getValue(), providerBolt.getValue());
        assertEquals(insertBolt.getType(), providerBolt.getType());

        Bolt updateBolt = new Bolt(providerBolt.getId(), providerBolt.getType(), providerBolt.getKey(), providerBolt.getValue() + providerBolt.getValue());

        int update = contentResolver.update(WrenchProviderContract.boltUri(updateBolt.getId()), updateBolt.toContentValues(), null, null);
        assertEquals(1, update);

        cursor = contentResolver.query(WrenchProviderContract.boltUri(updateBoltKey), null, null, null, null);
        assertNotNull(cursor);

        assertTrue(cursor.moveToFirst());
        Bolt updatedBolt = Bolt.fromCursor(cursor);

        assertEquals(insertBolt.getValue() + insertBolt.getValue(), updatedBolt.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMissingInsertForBoltWithId() throws Exception {
        contentResolver.insert(WrenchProviderContract.boltUri(0), getBolt("dummyBolt").toContentValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMissingInsertForBoltWithKey() throws Exception {
        contentResolver.insert(WrenchProviderContract.boltUri("fake"), getBolt("dummyBolt").toContentValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMissingUpdateForBoltWithKey() throws Exception {
        contentResolver.update(WrenchProviderContract.boltUri("fake"), getBolt("dummyBolt").toContentValues(), null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMissingUpdateForBolts() throws Exception {
        contentResolver.update(WrenchProviderContract.boltUri(), getBolt("dummyBolt").toContentValues(), null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMissingUpdateForNuts() throws Exception {
        contentResolver.update(WrenchProviderContract.nutUri(), getNut("dummyNut").toContentValues(), null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMissingQueryForBolts() throws Exception {
        contentResolver.update(WrenchProviderContract.boltUri(), getBolt("dummyBolt").toContentValues(), null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMissingQueryForNuts() throws Exception {
        contentResolver.update(WrenchProviderContract.nutUri(), getNut("dummyNut").toContentValues(), null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMissingDeleteForBoltWithId() throws Exception {
        contentResolver.delete(WrenchProviderContract.boltUri(0), null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMissingDeleteForBoltWithKey() throws Exception {
        contentResolver.delete(WrenchProviderContract.boltUri("fake"), null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMissingDeleteForBolts() throws Exception {
        contentResolver.delete(WrenchProviderContract.boltUri(), null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMissingDeleteForNuts() throws Exception {
        contentResolver.delete(WrenchProviderContract.nutUri(), null, null);
    }

    private Nut getNut(String value) {
        return new Nut(0, value);
    }

    private Bolt getBolt(String key) {
        return new Bolt(0, "bolttype", key, "boltvalue");
    }
}