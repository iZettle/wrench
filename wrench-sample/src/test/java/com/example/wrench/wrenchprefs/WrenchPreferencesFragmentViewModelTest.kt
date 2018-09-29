package com.example.wrench.wrenchprefs

import android.content.res.Resources
import com.example.wrench.MyEnum
import com.izettle.wrench.preferences.WrenchPreferences
import junit.framework.Assert.assertFalse
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class WrenchPreferencesFragmentViewModelTest {

    private var wrenchPreferencesViewModel: WrenchPreferencesFragmentViewModel? = null

    @Mock
    private val wrenchPreferences: WrenchPreferences? = null
    @Mock
    private val resources: Resources? = null

    @Before
    fun initialise() {
        MockitoAnnotations.initMocks(this)
        wrenchPreferencesViewModel = WrenchPreferencesFragmentViewModel(resources!!, wrenchPreferences!!)
    }

    @Test
    @Throws(Exception::class)
    fun getIntConfiguration() {
        `when`(resources!!.getString(anyInt())).thenReturn("asd")
        `when`(wrenchPreferences!!.getInt(ArgumentMatchers.anyString(), anyInt())).thenReturn(1)
        val result = wrenchPreferencesViewModel!!.getIntConfiguration()
        Assert.assertNotNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun getStringConfiguration() {
        `when`(resources!!.getString(anyInt())).thenReturn("asd")
        `when`<String>(wrenchPreferences!!.getString(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn("123")
        val result = wrenchPreferencesViewModel!!.getStringConfiguration()
        Assert.assertEquals("123", result)
    }

    @Test
    @Throws(Exception::class)
    fun getBooleanConfiguration() {
        `when`(resources!!.getString(anyInt())).thenReturn("asd")
        `when`(wrenchPreferences!!.getBoolean(ArgumentMatchers.anyString(), anyBoolean())).thenReturn(false)
        val result = wrenchPreferencesViewModel!!.getBooleanConfiguration()
        Assert.assertNotNull(result)
        assertFalse(result.value!!)
    }

    @Test
    @Throws(Exception::class)
    fun getEnumConfiguration() {
        `when`(resources!!.getString(anyInt())).thenReturn("asd")
        `when`(wrenchPreferences!!.getEnum(ArgumentMatchers.anyString(), eq<Class<MyEnum>>(MyEnum::class.java), any(MyEnum::class.java!!))).thenReturn(MyEnum.THIRD)
        val result = wrenchPreferencesViewModel!!.getEnumConfiguration()
        Assert.assertEquals(MyEnum.THIRD, result)
    }
}
