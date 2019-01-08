package com.izettle.wrench.provider

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.izettle.wrench.BuildConfig
import com.izettle.wrench.core.Bolt
import com.izettle.wrench.core.Nut
import com.izettle.wrench.core.WrenchProviderContract
import com.izettle.wrench.database.WrenchDatabase
import com.izettle.wrench.di.sampleAppModule
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.robolectric.Robolectric

val roomTestModule = module {
    single(override = true) {
        // In-Memory database config
        val lol = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), WrenchDatabase::class.java).allowMainThreadQueries().build()
        lol
    }

    single(override = true) {
        TestPackageManagerWrapper("TestApplication", "com.test.application") as IPackageManagerWrapper
    }
}

@RunWith(AndroidJUnit4::class)
class WrenchProviderTest : AutoCloseKoinTest() {

    private lateinit var wrenchProvider: WrenchProvider

    private var databass: WrenchDatabase? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        if (GlobalContext.getOrNull() == null) {
            startKoin(koinApplication {
                androidLogger(Level.DEBUG)
                modules(listOf(sampleAppModule))
                androidContext(ApplicationProvider.getApplicationContext())
            })
        }

        loadKoinModules(roomTestModule)

        databass = get<WrenchDatabase>()

        val contentProviderController = Robolectric.buildContentProvider(WrenchProvider::class.java).create(BuildConfig.CONFIG_AUTHORITY)
        wrenchProvider = contentProviderController.get()
    }

    @After
    fun tearDown() {
        databass!!.close()
    }

    @Test
    fun testInsertBolt() {
        val insertBoltKey = "insertBoltKey"

        val uri = WrenchProviderContract.boltUri()
        val insertBolt = getBolt(insertBoltKey)
        val insertBoltUri = wrenchProvider.insert(uri, insertBolt.toContentValues())
        Assert.assertNotNull(insertBoltUri)

        wrenchProvider.query(WrenchProviderContract.boltUri(insertBoltKey), null, null, null, null).use { cursor ->

            Assert.assertNotNull(cursor)
            Assert.assertEquals(1, cursor!!.count)

            cursor.moveToFirst()
            val queryBolt = Bolt.fromCursor(cursor)

            Assert.assertEquals(insertBolt.key, queryBolt.key)
            Assert.assertEquals(insertBolt.value, queryBolt.value)
            Assert.assertEquals(insertBolt.type, queryBolt.type)
        }

        wrenchProvider.query(WrenchProviderContract.boltUri(Integer.parseInt(insertBoltUri!!.lastPathSegment!!).toLong()), null, null, null, null).use { cursor ->

            Assert.assertNotNull(cursor)
            Assert.assertEquals(1, cursor!!.count)

            cursor.moveToFirst()
            val queryBolt = Bolt.fromCursor(cursor)

            Assert.assertEquals(insertBolt.key, queryBolt.key)
            Assert.assertEquals(insertBolt.value, queryBolt.value)
            Assert.assertEquals(insertBolt.type, queryBolt.type)
        }
    }

    @Test
    fun testUpdateBolt() {
        val updateBoltKey = "updateBoltKey"

        val uri = WrenchProviderContract.boltUri()
        val insertBolt = getBolt(updateBoltKey)
        val insertBoltUri = wrenchProvider.insert(uri, insertBolt.toContentValues())
        Assert.assertNotNull(insertBoltUri)

        wrenchProvider.query(WrenchProviderContract.boltUri(updateBoltKey), null, null, null, null).use { cursor ->

            Assert.assertNotNull(cursor)
            Assert.assertTrue(cursor!!.moveToFirst())

            val providerBolt = Bolt.fromCursor(cursor)
            Assert.assertEquals(insertBolt.key, providerBolt.key)
            Assert.assertEquals(insertBolt.value, providerBolt.value)
            Assert.assertEquals(insertBolt.type, providerBolt.type)

            val updateBolt = Bolt(providerBolt.id!!, providerBolt.type, providerBolt.key, providerBolt.value!! + providerBolt.value!!)

            val update = wrenchProvider.update(WrenchProviderContract.boltUri(updateBolt.id!!), updateBolt.toContentValues(), null, null)
            Assert.assertEquals(1, update)
        }

        wrenchProvider.query(WrenchProviderContract.boltUri(updateBoltKey), null, null, null, null).use { cursor ->

            Assert.assertNotNull(cursor)

            Assert.assertTrue(cursor!!.moveToFirst())
            val updatedBolt = Bolt.fromCursor(cursor)

            Assert.assertEquals(insertBolt.value!! + insertBolt.value!!, updatedBolt.value)
        }
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingInsertForBoltWithId() {
        wrenchProvider.insert(WrenchProviderContract.boltUri(0), getBolt("dummyBolt").toContentValues())
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingInsertForBoltWithKey() {
        wrenchProvider.insert(WrenchProviderContract.boltUri("fake"), getBolt("dummyBolt").toContentValues())
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingUpdateForBoltWithKey() {
        wrenchProvider.update(WrenchProviderContract.boltUri("fake"), getBolt("dummyBolt").toContentValues(), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingUpdateForBolts() {
        wrenchProvider.update(WrenchProviderContract.boltUri(), getBolt("dummyBolt").toContentValues(), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingUpdateForNuts() {
        wrenchProvider.update(WrenchProviderContract.nutUri(), getNut("dummyNut").toContentValues(), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingQueryForBolts() {
        wrenchProvider.update(WrenchProviderContract.boltUri(), getBolt("dummyBolt").toContentValues(), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingQueryForNuts() {
        wrenchProvider.update(WrenchProviderContract.nutUri(), getNut("dummyNut").toContentValues(), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingDeleteForBoltWithId() {
        wrenchProvider.delete(WrenchProviderContract.boltUri(0), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingDeleteForBoltWithKey() {
        wrenchProvider.delete(WrenchProviderContract.boltUri("fake"), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingDeleteForBolts() {
        wrenchProvider.delete(WrenchProviderContract.boltUri(), null, null)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testMissingDeleteForNuts() {
        wrenchProvider.delete(WrenchProviderContract.nutUri(), null, null)
    }

    private fun getNut(value: String): Nut {
        return Nut(0, value)
    }

    private fun getBolt(key: String): Bolt {
        return Bolt(0L, "bolttype", key, "boltvalue")
    }
}