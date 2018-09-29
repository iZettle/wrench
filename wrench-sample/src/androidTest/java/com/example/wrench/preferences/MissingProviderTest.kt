package com.example.wrench.preferences

import android.content.ContentResolver
import android.database.Cursor
import android.test.mock.MockContentResolver
import android.test.mock.MockContext

import com.izettle.wrench.core.WrenchProviderContract
import com.izettle.wrench.preferences.WrenchPreferences

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.runner.AndroidJUnit4

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNull

@RunWith(AndroidJUnit4::class)
class MissingProviderTest {

    private var mockContext: ResolverMockContext? = null

    @Before
    fun setUp() {
        mockContext = ResolverMockContext()

        val query = mockContext!!.contentResolver.query(WrenchProviderContract.boltUri(""), null, null, null, null)
        assertNull(query)
    }

    @Test
    fun checkBoolean() {
        val key = "boolean"

        val wrenchPreferences = WrenchPreferences(mockContext!!)
        assertFalse(wrenchPreferences.getBoolean(key, false))
        assertFalse(wrenchPreferences.getBoolean(key, false))
    }

    @Test
    fun checkInteger() {
        val key = "integer"
        val wrenchPreferences = WrenchPreferences(mockContext!!)
        assertEquals(42, wrenchPreferences.getInt(key, 42))
        assertEquals(1, wrenchPreferences.getInt(key, 1))
    }

    @Test
    fun checkString() {
        val key = "string"
        val wrenchPreferences = WrenchPreferences(mockContext!!)
        assertEquals("correct", wrenchPreferences.getString(key, "correct"))
        assertEquals("invalid", wrenchPreferences.getString(key, "invalid"))
    }

    @Test
    fun checkEnum() {
        val key = "enum"

        val wrenchPreferences = WrenchPreferences(mockContext!!)
        assertEquals(MyEnum.FIRST, wrenchPreferences.getEnum(key, MyEnum::class.java!!, MyEnum.FIRST))
        assertEquals(MyEnum.SECOND, wrenchPreferences.getEnum(key, MyEnum::class.java!!, MyEnum.SECOND))
    }

    internal enum class MyEnum {
        FIRST, SECOND
    }

    private inner class ResolverMockContext : MockContext() {
        private val contentResolver: MockContentResolver

        init {
            contentResolver = MockContentResolver()
        }

        override fun getContentResolver(): ContentResolver {
            return contentResolver
        }
    }
}
