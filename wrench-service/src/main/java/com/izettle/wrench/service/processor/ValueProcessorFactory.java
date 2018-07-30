package com.izettle.wrench.service.processor;

import com.izettle.wrench.service.AnnotationProcessor;
import com.izettle.wrench.service.internal.Factory;

public class ValueProcessorFactory implements Factory<AnnotationProcessor<Object>> {

    @Override
    public AnnotationProcessor<Object> create() {
        return new ValueProcessor();
    }
}
