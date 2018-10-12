package com.izettle.wrench.service;

import com.izettle.wrench.service.helper.Invokator;
import com.izettle.wrench.service.helper.Invokator.Invokation;
import com.izettle.wrench.service.helper.TestInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.izettle.wrench.service.helper.TestInterface.Test.VALUE;

public class KeyProcessorTest {

    public KeyProcessor processor;

    @Before
    public void setUp() {
        processor = new KeyProcessor();
    }

    @Test(expected = NullPointerException.class)
    public void getValue_NullMethod_ShouldThrows() {
        processor.getValue(null, new Object[]{});
    }

    @Test
    public void getValue_MissingAnnotation_ShouldThrow() {
        final List<Invokation> invokations = Arrays.asList(
                Invokator.invoke(TestInterface.class, (i) -> i.intReturnTypeMissingKey()),
                Invokator.invoke(TestInterface.class, (i) -> i.stringReturnTypeMissingKey()),
                Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnTypeMissingKey()),
                Invokator.invoke(TestInterface.class, (i) -> i.enumReturnTypeWithDynamicValueMissingKey(VALUE)),
                Invokator.invoke(TestInterface.class, (i) -> i.intReturnTypeWithDynamicValueMissingKey(1)),
                Invokator.invoke(TestInterface.class, (i) -> i.stringReturnTypeWithDynamicValueMissingKey("")),
                Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnTypeWithDynamicValueMissingKey(true))
        );

        for (final Invokation invokation : invokations) {
            try {
                processor.getValue(invokation.method, invokation.args);
                Assert.fail(invokation.method.getName());
            } catch (IllegalArgumentException e) {
            }
        }
    }

    @Test
    public void getValue_PresentAnnotation_ShouldReturnValue() {
        final List<Invokation> invokations = Arrays.asList(
                Invokator.invoke(TestInterface.class, (i) -> i.intReturnType()),
                Invokator.invoke(TestInterface.class, (i) -> i.stringReturnType()),
                Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnType()),
                Invokator.invoke(TestInterface.class, (i) -> i.enumReturnTypeWithDynamicValue(VALUE)),
                Invokator.invoke(TestInterface.class, (i) -> i.intReturnTypeWithDynamicValue(1)),
                Invokator.invoke(TestInterface.class, (i) -> i.stringReturnTypeWithDynamicValue("")),
                Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnTypeWithDynamicValue(true))
        );

        for (final Invokation invokation : invokations) {
            final String key = processor.getValue(invokation.method, invokation.args);
            Assert.assertEquals(key, invokation.method.getName());
        }
    }

    @Test
    public void getValue_PresentAnnotation_NullArgs_ShouldReturnValue() {
        final List<Invokation> invokations = Arrays.asList(
                Invokator.invoke(TestInterface.class, (i) -> i.intReturnType()),
                Invokator.invoke(TestInterface.class, (i) -> i.stringReturnType()),
                Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnType()),
                Invokator.invoke(TestInterface.class, (i) -> i.enumReturnTypeWithDynamicValue(VALUE)),
                Invokator.invoke(TestInterface.class, (i) -> i.intReturnTypeWithDynamicValue(1)),
                Invokator.invoke(TestInterface.class, (i) -> i.stringReturnTypeWithDynamicValue("")),
                Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnTypeWithDynamicValue(true))
        );

        for (final Invokation invokation : invokations) {
            final String key = processor.getValue(invokation.method, null);
            Assert.assertEquals(key, invokation.method.getName());
        }
    }
}