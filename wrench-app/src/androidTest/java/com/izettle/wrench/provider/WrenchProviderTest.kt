package com.izettle.wrench.provider

import android.test.ProviderTestCase2
import android.test.mock.MockContentResolver
import androidx.room.Room
import androidx.test.runner.AndroidJUnit4
import com.izettle.wrench.BuildConfig
import com.izettle.wrench.core.Bolt
import com.izettle.wrench.core.Nut
import com.izettle.wrench.core.WrenchProviderContract
import com.izettle.wrench.database.WrenchDatabase
import com.izettle.wrench.preferences.WrenchPreferences
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class WrenchProviderTest : ProviderTestCase2<WrenchProvider>(WrenchProvider::class.java, BuildConfig.CONFIG_AUTHORITY) {

    private var contentResolver: MockContentResolver? = null

    @Before
    @Throws(Exception::class)
    public override fun setUp() {
        super.setUp()
        contentResolver = mockContentResolver

        val wrenchDatabase = Room.inMemoryDatabaseBuilder(mockContext, WrenchDatabase::class.java).build()

        provider.applicationDao = wrenchDatabase.applicationDao()
        provider.configurationDao = wrenchDatabase.configurationDao()
        provider.configurationValueDao = wrenchDatabase.configurationValueDao()
        provider.scopeDao = wrenchDatabase.scopeDao()
        provider.predefinedConfigurationDao = wrenchDatabase.predefinedConfigurationValueDao()
        provider.wrenchPreferences = mock(WrenchPreferences::class.java)

        provider.packageManagerWrapper = TestPackageManagerWrapper("TestApplication", "com.test.application")

    }

    @Test
    fun testInsertBolt() {
        val insertBoltKey = "insertBoltKey"

        val uri = WrenchProviderContract.boltUri()
        val insertBolt = getBolt(insertBoltKey)
        val insertBoltUri = contentResolver!!.insert(uri, insertBolt.toContentValues())
        Assert.assertNotNull(insertBoltUri)

        var cursor = contentResolver!!.query(WrenchProviderContract.boltUri(insertBoltKey), null, null, null, null)
        Assert.assertNotNull(cursor)
        Assert.assertEquals(1, cursor!!.count)

        cursor.moveToFirst()
        var queryBolt = Bolt.fromCursor(cursor)

        Assert.assertEquals(insertBolt.key, queryBolt.key)
        Assert.assertEquals(insertBolt.value, queryBolt.value)
        Assert.assertEquals(insertBolt.type, queryBolt.type)

        cursor = contentResolver!!.query(WrenchProviderContract.boltUri(Integer.parseInt(insertBoltUri!!.lastPathSegment!!).toLong()), null, null, null, null)
        Assert.assertNotNull(cursor)
        Assert.assertEquals(1, cursor!!.count)

        cursor.moveToFirst()
        queryBolt = Bolt.fromCursor(cursor)

        Assert.assertEquals(insertBolt.key, queryBolt.key)
        Assert.assertEquals(insertBolt.value, queryBolt.value)
        Assert.assertEquals(insertBolt.type, queryBolt.type)
    }

    @Test
    fun testUpdateBolt() {
        val updateBoltKey = "updateBoltKey"

        val uri = WrenchProviderContract.boltUri()
        val insertBolt = getBolt(updateBoltKey)
        val insertBoltUri = contentResolver!!.insert(uri, insertBolt.toContentValues())
        Assert.assertNotNull(insertBoltUri)

        var cursor = contentResolver!!.query(WrenchProviderContract.boltUri(updateBoltKey), null, null, null, null)
        Assert.assertNotNull(cursor)
        Assert.assertTrue(cursor!!.moveToFirst())

        val providerBolt = Bolt.fromCursor(cursor)
        Assert.assertEquals(insertBolt.key, providerBolt.key)
        Assert.assertEquals(insertBolt.value, providerBolt.value)
        Assert.assertEquals(insertBolt.type, providerBolt.type)

        val updateBolt = Bolt(providerBolt.id!!, providerBolt.type, providerBolt.key, providerBolt.value!! + providerBolt.value!!)

        val update = contentResolver!!.update(WrenchProviderContract.boltUri(updateBolt.id!!), updateBolt.toContentValues(), null, null)
        Assert.assertEquals(1, update)

        cursor = contentResolver!!.query(WrenchProviderContract.boltUri(updateBoltKey), null, null, null, null)
        Assert.assertNotNull(cursor)

        Assert.assertTrue(cursor!!.moveToFirst())
        val updatedBolt = Bolt.fromCursor(cursor)

        Assert.assertEquals(insertBolt.value!! + insertBolt.value!!, updatedBolt.value)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingInsertForBoltWithId() {
        contentResolver!!.insert(WrenchProviderContract.boltUri(0), getBolt("dummyBolt").toContentValues())
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingInsertForBoltWithKey() {
        contentResolver!!.insert(WrenchProviderContract.boltUri("fake"), getBolt("dummyBolt").toContentValues())
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingUpdateForBoltWithKey() {
        contentResolver!!.update(WrenchProviderContract.boltUri("fake"), getBolt("dummyBolt").toContentValues(), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingUpdateForBolts() {
        contentResolver!!.update(WrenchProviderContract.boltUri(), getBolt("dummyBolt").toContentValues(), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingUpdateForNuts() {
        contentResolver!!.update(WrenchProviderContract.nutUri(), getNut("dummyNut").toContentValues(), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingQueryForBolts() {
        contentResolver!!.update(WrenchProviderContract.boltUri(), getBolt("dummyBolt").toContentValues(), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingQueryForNuts() {
        contentResolver!!.update(WrenchProviderContract.nutUri(), getNut("dummyNut").toContentValues(), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingDeleteForBoltWithId() {
        contentResolver!!.delete(WrenchProviderContract.boltUri(0), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingDeleteForBoltWithKey() {
        contentResolver!!.delete(WrenchProviderContract.boltUri("fake"), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingDeleteForBolts() {
        contentResolver!!.delete(WrenchProviderContract.boltUri(), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingDeleteForNuts() {
        contentResolver!!.delete(WrenchProviderContract.nutUri(), null, null)
    }

    private fun getNut(value: String): Nut {
        return Nut(0, value)
    }

    private fun getBolt(key: String): Bolt {
        return Bolt(0L, "bolttype", key, "boltvalue")
    }
}