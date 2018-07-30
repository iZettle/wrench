package com.izettle.wrench.service.processor;

import com.izettle.wrench.service.AnnotationProcessor;
import com.izettle.wrench.service.Key;
import com.izettle.wrench.service.internal.Factory;

import java.lang.reflect.Method;

public class KeyProcessorFactory implements Factory<AnnotationProcessor<String>> {

    @Override
    public AnnotationProcessor<String> create() {
        return new KeyProcessor();
    }
}
