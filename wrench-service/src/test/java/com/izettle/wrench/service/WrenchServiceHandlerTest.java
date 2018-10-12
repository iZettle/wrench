package com.izettle.wrench.service;

import android.os.Build;

import com.izettle.wrench.service.helper.Invokator;
import com.izettle.wrench.service.helper.Invokator.Invokation;
import com.izettle.wrench.service.helper.StaticField;
import com.izettle.wrench.service.helper.TestInterface;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WrenchServiceHandlerTest {

    public Provider provider;
    public AnnotationProcessor keyProcessor;
    public AnnotationProcessor valueProcessor;
    public WrenchServiceHandler handler;

    @Before
    public void setUp() throws Exception {
        provider = mock(Provider.class);
        keyProcessor = mock(AnnotationProcessor.class);
        valueProcessor = mock(AnnotationProcessor.class);
        handler = new WrenchServiceHandler(provider, keyProcessor, valueProcessor);
        StaticField.setFinalStatic(Build.VERSION.class.getField("SDK_INT"), Build.VERSION_CODES.N);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void invoke_DefaultMethod_ShouldThrows() throws Throwable {
        final String value = "invoke_DefaultMethod_ShouldThrows";
        final Invokation inv = Invokator.invoke(TestInterface.class, (i) -> i.objectReturnTypeDefaultMethod(value));
        handler.invoke(handler, inv.method, inv.args);
    }


    @Test(expected = NullPointerException.class)
    public void invoke_NullMethod_ShouldThrows() throws Throwable {
        final String value = "invoke_NullMethod_ShouldThrows";
        final Invokation inv = Invokator.invoke(TestInterface.class, (i) -> i.objectReturnTypeDefaultMethod(value));
        handler.invoke(handler, null, inv.args);
    }

    @Test
    public void invoke_ObjectMethod_ShouldReturnDefaultImplementation() throws Throwable {
        final String value = handler.toString();
        final Invokation inv = Invokator.invoke(TestInterface.class, (i) -> i.toString());
        assertEquals(value, handler.invoke(handler, inv.method, inv.args));
    }

    @Test
    public void invoke_CompleteMethod_ShouldDelegateHandling() throws Throwable {
        final String key = "booleanReturnType";
        final Class returnType = Boolean.TYPE;
        final boolean value = true;

        final Invokation inv = Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnType());

        when(keyProcessor.getValue(eq(inv.method), eq(inv.args))).thenReturn(key);
        when(valueProcessor.getValue(eq(inv.method), eq(inv.args))).thenReturn(value);
        when(provider.getValue(eq(returnType), eq(key), eq(true))).thenReturn(value);

        assertEquals(value, handler.invoke(handler, inv.method, inv.args));

        verify(keyProcessor, times(1)).getValue(eq(inv.method), eq(inv.args));
        verify(valueProcessor, times(1)).getValue(eq(inv.method), eq(inv.args));
        verify(provider, times(1)).getValue(eq(returnType), eq(key), eq(value));
    }
}