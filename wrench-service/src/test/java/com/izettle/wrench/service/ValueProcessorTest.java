package com.izettle.wrench.service;

import com.izettle.wrench.service.ValueProcessor;
import com.izettle.wrench.service.helper.Invokator;
import com.izettle.wrench.service.helper.Invokator.Invokation;
import com.izettle.wrench.service.helper.TestInterface;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.izettle.wrench.service.helper.TestInterface.Test.VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ValueProcessorTest {

    public ValueProcessor processor;

    @Before
    public void setUp() throws Exception {
        processor = new ValueProcessor();
    }

    @Test(expected = NullPointerException.class)
    public void getValue_NullMethod_ShouldThrows() {
        processor.getValue(null, new Object[]{});
    }

    @Test
    public void getValue_MissingAnnotation_ShouldThrow() {
        final List<Invokation> invokations = Arrays.asList(
                Invokator.invoke(TestInterface.class, (i) -> i.intReturnTypeMissingDefaultValue()),
                Invokator.invoke(TestInterface.class, (i) -> i.stringReturnTypeMissingDefaultValue()),
                Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnTypeMissingDefaultValue()),
                Invokator.invoke(TestInterface.class, (i) -> i.enumReturnTypeWithDynamicValueMissingDefaultValue(VALUE)),
                Invokator.invoke(TestInterface.class, (i) -> i.intReturnTypeWithDynamicValueMissingDefaultValue(1)),
                Invokator.invoke(TestInterface.class, (i) -> i.stringReturnTypeWithDynamicValueMissingDefaultValue("")),
                Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnTypeWithDynamicValueMissingDefaultValue(true))
        );

        for (final Invokation invokation : invokations) {
            try {
                processor.getValue(invokation.method, invokation.args);
                fail(invokation.method.getName());
            } catch (IllegalArgumentException e) {
            }
        }
    }

    @Test
    public void getValue_PresentAnnotationOnMethod_ShouldReturnValue() {
        final Map<Invokation, Object> invokations = new HashMap<>();
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.intReturnType()), 1);
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.stringReturnType()), "stringReturnType");
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnType()), true);

        for (final Map.Entry<Invokation, Object> entry : invokations.entrySet()) {
            Invokation invokation = entry.getKey();
            Object expected = entry.getValue();
            final Object object = processor.getValue(invokation.method, invokation.args);
            assertEquals(expected, object);
        }
    }

    @Test
    public void getValue_PresentAnnotationOnSignature_ShouldReturnValue() {
        final Map<Invokation, Object> invokations = new HashMap<>();
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.enumReturnTypeWithDynamicValue(VALUE)), VALUE);
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.enumReturnTypeWithDynamicValue(null)), null);
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.intReturnTypeWithDynamicValue(1)), 1);
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.stringReturnTypeWithDynamicValue("stringReturnTypeWithDynamicValue")), "stringReturnTypeWithDynamicValue");
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.stringReturnTypeWithDynamicValue(null)), null);
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnTypeWithDynamicValue(true)), true);

        for (final Map.Entry<Invokation, Object> entry : invokations.entrySet()) {
            Invokation invokation = entry.getKey();
            Object expected = entry.getValue();
            final Object object = processor.getValue(invokation.method, invokation.args);
            assertEquals(expected, object);
        }
    }

    @Test
    public void getValue_PresentAnnotationOnMethod_NullArgs_ShouldReturnValue() {
        final Map<Invokation, Object> invokations = new HashMap<>();
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.intReturnType()), 1);
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.stringReturnType()), "stringReturnType");
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnType()), true);

        for (final Map.Entry<Invokation, Object> entry : invokations.entrySet()) {
            Invokation invokation = entry.getKey();
            Object expected = entry.getValue();
            final Object object = processor.getValue(invokation.method, null);
            assertEquals(expected, object);
        }
    }

    @Test(expected = NullPointerException.class)
    public void getValue_PresentAnnotationOnSignature_NullArgs_ShouldThrows() {
        final Map<Invokation, Object> invokations = new HashMap<>();
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.enumReturnTypeWithDynamicValue(VALUE)), VALUE);
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.enumReturnTypeWithDynamicValue(null)), null);
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.intReturnTypeWithDynamicValue(1)), 1);
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.stringReturnTypeWithDynamicValue("stringReturnTypeWithDynamicValue")), "stringReturnTypeWithDynamicValue");
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.stringReturnTypeWithDynamicValue(null)), null);
        invokations.put(Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnTypeWithDynamicValue(true)), true);

        for (final Map.Entry<Invokation, Object> entry : invokations.entrySet()) {
            Invokation invokation = entry.getKey();
            Object expected = entry.getValue();
            final Object object = processor.getValue(invokation.method,null);
            assertEquals(expected, object);
        }
    }
}