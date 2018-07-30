package com.izettle.wrench.service.storage;

import com.izettle.wrench.preferences.WrenchPreferences;
import com.izettle.wrench.service.helper.TestInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrenchPreferenceStorageTest {

    public WrenchPreferences wrenchPreferences;
    public WrenchPreferenceStorage storage;

    @Before
    public void setUp() throws Exception {
        wrenchPreferences = mock(WrenchPreferences.class);
        storage = new WrenchPreferenceStorage(wrenchPreferences);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getValue_NotSupportedType_ShouldThrows() {
        storage.getValue(Void.class, "", new Object());
    }

    @Test(expected = NullPointerException.class)
    public void getValue_NullType_ShouldThrows() {
        storage.getValue(null, "", new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getValue_NullKey_ShouldThrows() {
        when(wrenchPreferences.getString(eq(null), anyString())).thenThrow(IllegalArgumentException.class);
        storage.getValue(String.class, null, "");
    }

    @Test
    public void getValue_EverythingValid_ShouldReturnValue() {
        final Holder<String> testString = new Holder("testString", "testString", String.class);
        when(wrenchPreferences.getString(eq(testString.key), eq(testString.defaultValue))).thenReturn(testString.defaultValue);
        final Holder<String> testStringNull = new Holder("testStringNull", null, String.class);
        when(wrenchPreferences.getString(eq(testStringNull.key), eq(testStringNull.defaultValue))).thenReturn(testStringNull.defaultValue);

        final Holder<Integer> testPrimitiveInt = new Holder("testPrimitiveInt", 0, Integer.TYPE);
        when(wrenchPreferences.getInt(eq(testPrimitiveInt.key), eq(testPrimitiveInt.defaultValue))).thenReturn(testPrimitiveInt.defaultValue);
        final Holder<Integer> testInteger = new Holder("testInteger", new Integer(0), Integer.class);
        when(wrenchPreferences.getInt(eq(testInteger.key), eq(testInteger.defaultValue))).thenReturn(testInteger.defaultValue);

        final Holder<Boolean> testPrimitiveBoolean = new Holder("testPrimitiveBoolean", true, Boolean.TYPE);
        when(wrenchPreferences.getBoolean(eq(testPrimitiveBoolean.key), eq(testPrimitiveBoolean.defaultValue))).thenReturn(testPrimitiveBoolean.defaultValue);
        final Holder<Boolean> testBoolean = new Holder("testBoolean", new Boolean(true), Boolean.class);
        when(wrenchPreferences.getBoolean(eq(testBoolean.key), eq(testBoolean.defaultValue))).thenReturn(testBoolean.defaultValue);

        final Holder<TestInterface.Test> testEnum = new Holder("testEnum", TestInterface.Test.VALUE, TestInterface.Test.class);
        when(wrenchPreferences.getEnum(eq(testEnum.key), eq(testEnum.returnType), eq(testEnum.defaultValue))).thenReturn(testEnum.defaultValue);
        final Holder<TestInterface.Test> testEnumNull = new Holder("testEnumNull", null, TestInterface.Test.class);
        when(wrenchPreferences.getEnum(eq(testEnumNull.key), eq(testEnumNull.returnType), eq(testEnumNull.defaultValue))).thenReturn(testEnumNull.defaultValue);

        final List<Holder> list = Arrays.asList(
                testString,
                testStringNull,
                testPrimitiveInt,
                testInteger,
                testPrimitiveBoolean,
                testBoolean,
                testEnum,
                testEnumNull
        );

        for (final Holder holder : list) {
            final Object value = storage.getValue(holder.returnType, holder.key, holder.defaultValue);
            Assert.assertEquals(holder.expectedValue, value);
        }
    }

    private static class Holder<T> {
        public final String key;
        public final T defaultValue;
        public final T expectedValue;
        public final Class<T> returnType;

        private Holder(String key, T defaultValue, Class<T> returnType) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.returnType = returnType;
            this.expectedValue = defaultValue;
        }
    }
}