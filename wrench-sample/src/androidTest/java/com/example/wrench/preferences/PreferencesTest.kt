package com.example.wrench.preferences

import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.test.mock.MockContentProvider
import android.test.mock.MockContentResolver
import android.test.mock.MockContext
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.izettle.wrench.core.Bolt
import com.izettle.wrench.core.ColumnNames
import com.izettle.wrench.core.WrenchProviderContract
import com.izettle.wrench.preferences.WrenchPreferences
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val CURRENT_CONFIGURATION_ID = 1
private const val CURRENT_CONFIGURATION_KEY = 2
private const val CURRENT_CONFIGURATIONS = 3
private const val PREDEFINED_CONFIGURATION_VALUES = 5

@RunWith(AndroidJUnit4::class)
class PreferencesTest {

    private var mockContext: ResolverMockContext? = null

    @Before
    fun setUp() {
        mockContext = ResolverMockContext(InstrumentationRegistry.getContext())
    }

    @Test
    fun checkBoolean() {
        val key = "boolean"
        val wrenchPreferences = WrenchPreferences(mockContext!!)
        assertFalse(wrenchPreferences.getBoolean(key, false))
        assertFalse(wrenchPreferences.getBoolean(key, true))
    }

    @Test
    fun checkInteger() {
        val key = "integer"
        val wrenchPreferences = WrenchPreferences(mockContext!!)
        assertEquals(42, wrenchPreferences.getInt(key, 42))
        assertEquals(42, wrenchPreferences.getInt(key, 1))
    }

    @Test
    fun checkString() {
        val key = "string"
        val wrenchPreferences = WrenchPreferences(mockContext!!)
        assertEquals("correct", wrenchPreferences.getString(key, "correct"))
        assertEquals("correct", wrenchPreferences.getString(key, "invalid"))
    }

    @Test
    fun checkEnum() {
        val key = "enum"

        val wrenchPreferences = WrenchPreferences(mockContext!!)
        assertEquals(MyEnum.FIRST, wrenchPreferences.getEnum(key, MyEnum::class.java!!, MyEnum.FIRST))
        assertEquals(MyEnum.FIRST, wrenchPreferences.getEnum(key, MyEnum::class.java!!, MyEnum.SECOND))
    }

    internal enum class MyEnum {
        FIRST, SECOND
    }

    private inner class ResolverMockContext(context: Context) : MockContext() {
        private val contentResolver: MockContentResolver
        private val mockProvider: MockContentProvider

        init {
            mockProvider = BoltProvider(context)
            contentResolver = MockContentResolver()
            contentResolver.addProvider(WrenchProviderContract.WRENCH_AUTHORITY, mockProvider)
        }

        override fun getContentResolver(): ContentResolver {
            return contentResolver
        }
    }

    private inner class BoltProvider(context: Context) : MockContentProvider(context) {
        private val boltCursor: MatrixCursor
        internal var uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            val columnNames = arrayOf(ColumnNames.Bolt.COL_ID, ColumnNames.Bolt.COL_KEY, ColumnNames.Bolt.COL_TYPE, ColumnNames.Bolt.COL_VALUE)
            boltCursor = MatrixCursor(columnNames)

            uriMatcher.addURI(WrenchProviderContract.WRENCH_AUTHORITY, "currentConfiguration/#", CURRENT_CONFIGURATION_ID)
            uriMatcher.addURI(WrenchProviderContract.WRENCH_AUTHORITY, "currentConfiguration/*", CURRENT_CONFIGURATION_KEY)
            uriMatcher.addURI(WrenchProviderContract.WRENCH_AUTHORITY, "currentConfiguration", CURRENT_CONFIGURATIONS)
            uriMatcher.addURI(WrenchProviderContract.WRENCH_AUTHORITY, "predefinedConfigurationValue", PREDEFINED_CONFIGURATION_VALUES)

        }

        override fun query(uri: Uri?, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
            return boltCursor
        }

        override fun insert(uri: Uri?, values: ContentValues?): Uri? {
            when (uriMatcher.match(uri)) {
                CURRENT_CONFIGURATIONS -> {
                    val bolt = Bolt.fromContentValues(values!!)
                    bolt.id = (boltCursor.count + 1).toLong()
                    boltCursor.newRow()
                            .add(ColumnNames.Bolt.COL_ID, bolt.id)
                            .add(ColumnNames.Bolt.COL_KEY, bolt.key)
                            .add(ColumnNames.Bolt.COL_TYPE, bolt.type)
                            .add(ColumnNames.Bolt.COL_VALUE, bolt.value)
                    return ContentUris.withAppendedId(uri, bolt.id!!)
                }
                PREDEFINED_CONFIGURATION_VALUES -> {
                    return uri
                }
            }
            throw IllegalArgumentException("Unknown uri")
        }


    }
}
