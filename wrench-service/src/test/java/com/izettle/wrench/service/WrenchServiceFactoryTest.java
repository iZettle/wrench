package com.izettle.wrench.service;


import com.izettle.wrench.service.helper.TestInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class WrenchServiceFactoryTest {

    public Storage storage;
    public AnnotationProcessor<String> keyProcessor;
    public AnnotationProcessor<Object> valueProcessor;
    public WrenchServiceFactory factory;

    @Before
    public void setUp() throws Exception {
        storage = mock(Storage.class);
        keyProcessor = mock(AnnotationProcessor.class);
        valueProcessor = mock(AnnotationProcessor.class);
        factory = new WrenchServiceFactory(storage, keyProcessor, valueProcessor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_ProvideNonInterfaceClass_ShouldThrows() {
        factory.create(WrenchServiceFactoryTest.class);
    }

    @Test(expected = NullPointerException.class)
    public void create_ProvideNull_ShouldThrows() {
        factory.create(null);
    }

    @Test
    public void create_ProvideInterfaceClass_ShouldReturnNonNull() {
        final TestInterface testInterface = factory.create(TestInterface.class);
        assertNotNull(testInterface);
    }
}