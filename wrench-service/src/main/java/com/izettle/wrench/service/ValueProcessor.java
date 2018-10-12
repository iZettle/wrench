package com.izettle.wrench.service;

import com.izettle.wrench.service.AnnotationProcessor;
import com.izettle.wrench.service.DefaultValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class ValueProcessor implements AnnotationProcessor<Object> {

    @Override
    public Object getValue(final Method method, final Object[] args) {
        if (method.isAnnotationPresent(DefaultValue.Boolean.class)) {
            return method.getAnnotation(DefaultValue.Boolean.class).value();
        }

        if (method.isAnnotationPresent(DefaultValue.String.class)) {
            return method.getAnnotation(DefaultValue.String.class).value();
        }

        if (method.isAnnotationPresent(DefaultValue.Int.class)) {
            return method.getAnnotation(DefaultValue.Int.class).value();
        }

        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            final Annotation[] annotations = parameterAnnotations[i];
            for (int j = 0; j < annotations.length; j++) {
                final Annotation annotation = annotations[j];
                if (annotation instanceof DefaultValue) {
                    return args[i];
                }
            }
        }

        throw new IllegalArgumentException("Missing default value annotation");
    }
}
