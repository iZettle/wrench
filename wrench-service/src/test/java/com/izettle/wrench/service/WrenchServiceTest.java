package com.izettle.wrench.service;

import android.os.Build;

import com.izettle.wrench.service.helper.Invokator;
import com.izettle.wrench.service.helper.Invokator.Invokation;
import com.izettle.wrench.service.helper.StaticField;
import com.izettle.wrench.service.helper.TestInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WrenchServiceTest {

    public Storage storage;
    public AnnotationProcessor keyProcessor;
    public AnnotationProcessor valueProcessor;
    public WrenchService service;

    @Before
    public void setUp() throws Exception {
        storage = mock(Storage.class);
        keyProcessor = mock(AnnotationProcessor.class);
        valueProcessor = mock(AnnotationProcessor.class);
        service = new WrenchService(storage, keyProcessor, valueProcessor);
        StaticField.setFinalStatic(Build.VERSION.class.getField("SDK_INT"), Build.VERSION_CODES.N);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void invoke_DefaultMethod_ShouldThrows() throws Throwable {
        final String value = "invoke_DefaultMethod_ShouldThrows";
        final Invokation inv = Invokator.invoke(TestInterface.class, (i) -> i.objectReturnTypeDefaultMethod(value));
        service.invoke(service, inv.method, inv.args);
    }


    @Test(expected = NullPointerException.class)
    public void invoke_NullMethod_ShouldThrows() throws Throwable {
        final String value = "invoke_NullMethod_ShouldThrows";
        final Invokation inv = Invokator.invoke(TestInterface.class, (i) -> i.objectReturnTypeDefaultMethod(value));
        service.invoke(service, null, inv.args);
    }

    @Test
    public void invoke_ObjectMethod_ShouldReturnDefaultImplementation() throws Throwable {
        final String value = service.toString();
        final Invokation inv = Invokator.invoke(TestInterface.class, (i) -> i.toString());
        assertEquals(value, service.invoke(service, inv.method, inv.args));
    }

    @Test
    public void invoke_CompleteMethod_ShouldDelegateHandling() throws Throwable {
        final String key = "booleanReturnType";
        final Class returnType = Boolean.TYPE;
        final boolean value = true;

        final Invokation inv = Invokator.invoke(TestInterface.class, (i) -> i.booleanReturnType());

        when(keyProcessor.getValue(eq(inv.method), eq(inv.args))).thenReturn(key);
        when(valueProcessor.getValue(eq(inv.method), eq(inv.args))).thenReturn(value);
        when(storage.getValue(eq(returnType), eq(key), eq(true))).thenReturn(value);

        assertEquals(value, service.invoke(service, inv.method, inv.args));

        verify(keyProcessor, times(1)).getValue(eq(inv.method), eq(inv.args));
        verify(valueProcessor, times(1)).getValue(eq(inv.method), eq(inv.args));
        verify(storage, times(1)).getValue(eq(returnType), eq(key), eq(value));
    }
}