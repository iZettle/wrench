package com.izettle.wrench.service;


import com.izettle.wrench.service.helper.TestInterface;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class WrenchServiceTest {

    public Provider provider;
    public AnnotationProcessor<String> keyProcessor;
    public AnnotationProcessor<Object> valueProcessor;
    public WrenchService service;

    @Before
    public void setUp() throws Exception {
        provider = mock(Provider.class);
        keyProcessor = mock(AnnotationProcessor.class);
        valueProcessor = mock(AnnotationProcessor.class);
        service = new WrenchService(provider, keyProcessor, valueProcessor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_ProvideNonInterfaceClass_ShouldThrows() {
        service.create(WrenchServiceTest.class);
    }

    @Test(expected = NullPointerException.class)
    public void create_ProvideNull_ShouldThrows() {
        service.create(null);
    }

    @Test
    public void create_ProvideInterfaceClass_ShouldReturnNonNull() {
        final TestInterface testInterface = service.create(TestInterface.class);
        assertNotNull(testInterface);
    }
}