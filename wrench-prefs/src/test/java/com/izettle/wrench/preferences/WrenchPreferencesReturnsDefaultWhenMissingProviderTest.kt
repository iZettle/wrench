package com.izettle.wrench.preferences

import android.os.Build.VERSION_CODES.O
import androidx.test.runner.AndroidJUnit4
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [O])
class WrenchPreferencesReturnsDefaultWhenMissingProviderTest {

    private lateinit var wrenchPreferences: WrenchPreferences
    private val key = "myKey"

    private enum class TestEnum {
        FIRST, SECOND
    }

    @Before
    fun setUp() {
        wrenchPreferences = WrenchPreferences(RuntimeEnvironment.application)
    }

    @Test
    fun `always return default enum when missing backing content providerr`() {
        assertEquals(TestEnum.FIRST, wrenchPreferences.getEnum(key, TestEnum::class.java, TestEnum.FIRST))
        assertEquals(TestEnum.SECOND, wrenchPreferences.getEnum(key, TestEnum::class.java, TestEnum.SECOND))
    }

    @Test
    fun `always return default string when missing backing content providerr`() {
        assertEquals("first", wrenchPreferences.getString(key, "first"))
        assertEquals("second", wrenchPreferences.getString(key, "second"))
    }

    @Test
    fun `always return default boolean when missing backing content providerr`() {
        assertEquals(true, wrenchPreferences.getBoolean(key, true))
        assertEquals(false, wrenchPreferences.getBoolean(key, false))

    }

    @Test
    fun `always return default int when missing backing content providerr`() {
        assertEquals(1, wrenchPreferences.getInt(key, 1))
        assertEquals(2, wrenchPreferences.getInt(key, 2))
    }
}