package com.example.wrench.wrenchprefs

import android.content.res.Resources
import com.izettle.wrench.preferences.WrenchPreferences
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class WrenchPreferencesFragmentViewModelTest {

    lateinit var wrenchPreferencesViewModel: WrenchPreferencesFragmentViewModel

    @Before
    fun initialise() {
        val wrenchPreferences = mock(WrenchPreferences::class.java)
        val resources = mock(Resources::class.java)
        val wrenchPreferencesViewModel = WrenchPreferencesFragmentViewModel(resources, wrenchPreferences)
    }

    @Test
    fun testMocker() {
        // yep
    }

}