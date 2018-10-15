package com.izettle.wrench.service;

import java.lang.reflect.Method;

class KeyProcessor implements AnnotationProcessor<String> {

    @Override
    public String getValue(final Method method, final Object[] args) {
        if (method.isAnnotationPresent(Key.class)) {
            return method.getAnnotation(Key.class).value();
        }

        throw new IllegalArgumentException("Missing key annotation");
    }
}
