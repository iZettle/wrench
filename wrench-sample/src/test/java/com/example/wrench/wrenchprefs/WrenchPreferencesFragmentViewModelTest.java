package com.example.wrench.wrenchprefs;

import android.content.res.Resources;

import com.example.wrench.MyEnum;
import com.izettle.wrench.preferences.WrenchPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class WrenchPreferencesFragmentViewModelTest {

    private WrenchPreferencesFragmentViewModel wrenchPreferencesViewModel;

    @Mock
    private WrenchPreferences wrenchPreferences;
    @Mock
    private Resources resources;

    @Before
    public void initialise() {
        MockitoAnnotations.initMocks(this);
        wrenchPreferencesViewModel = new WrenchPreferencesFragmentViewModel(resources, wrenchPreferences);
    }

    @Test
    public void getIntConfiguration() {
        when(wrenchPreferences.getInt(anyString(), anyInt())).thenReturn(1);
        Integer result = wrenchPreferencesViewModel.getIntConfiguration();
        assertNotNull(result);
    }

    @Test
    public void getStringConfiguration() {
        when(resources.getString(anyInt())).thenReturn("asd");
        when(wrenchPreferences.getString(anyString(), anyString())).thenReturn("123");
        String result = wrenchPreferencesViewModel.getStringConfiguration();
        assertEquals("123", result);
    }

    @Test
    public void getBooleanConfiguration() {
        when(wrenchPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false);
        Boolean result = wrenchPreferencesViewModel.getBooleanConfiguration();
        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    public void getEnumConfiguration() {
        when(wrenchPreferences.getEnum(anyString(), eq(MyEnum.class), any(MyEnum.class))).thenReturn(MyEnum.SECOND);
        MyEnum result = wrenchPreferencesViewModel.getEnumConfiguration();
        assertEquals(null, result);
    }
}
