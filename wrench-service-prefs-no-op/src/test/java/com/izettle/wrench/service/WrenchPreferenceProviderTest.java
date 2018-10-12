package com.izettle.wrench.service;

import com.izettle.wrench.preferences.WrenchPreferences;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

public class WrenchPreferenceProviderTest {

    public WrenchPreferenceProvider provider;

    @Before
    public void setUp() {
        provider = new WrenchPreferenceProvider(mock(WrenchPreferences.class));
    }

    @Test
    public void getValue_AnyCase_ShouldReturnDefaultValue() {
        final List<Class<?>> returnTypes = Arrays.asList(
                String.class,
                Integer.TYPE,
                Integer.class,
                Boolean.TYPE,
                Boolean.class,
                TestEnum.class,
                null
        );

        final List<Object> defaultValues = Arrays.asList(
                "",
                1,
                new Integer(1),
                true,
                new Boolean(true),
                TestEnum.VALUE,
                null,
                new Object(),
                Void.class
        );

        final List<String> keys = Arrays.asList(
                "",
                null
        );

        returnTypes.stream()
                .flatMap(r -> defaultValues.stream().map(d -> new AbstractMap.SimpleEntry<>(r, d)))
                .flatMap(p -> keys.stream().map(k -> new Holder(k, p.getValue(), p.getKey())))
                .forEach(holder -> {

                    final Object value = provider.getValue(holder.returnType, holder.key, holder.defaultValue);
                    Assert.assertEquals(holder.expectedValue, value);
                });
    }

    private enum TestEnum {
        VALUE
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