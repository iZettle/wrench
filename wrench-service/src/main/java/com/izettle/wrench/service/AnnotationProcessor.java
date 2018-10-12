package com.izettle.wrench.service;

import java.lang.reflect.Method;

public interface AnnotationProcessor<T> {

    T getValue(final Method method, final Object[] args);
}
