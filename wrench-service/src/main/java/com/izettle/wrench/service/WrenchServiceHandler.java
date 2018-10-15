package com.izettle.wrench.service;

import android.annotation.SuppressLint;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class WrenchServiceHandler implements InvocationHandler {

    private final Provider mProvider;
    private final AnnotationProcessor<String> mKeyProcessor;
    private final AnnotationProcessor<Object> mValueProcessor;

    WrenchServiceHandler(final Provider provider,
                         final AnnotationProcessor<String> keyProcessor,
                         final AnnotationProcessor<Object> valueProcessor) {
        mProvider = provider;
        mKeyProcessor = keyProcessor;
        mValueProcessor = valueProcessor;
    }

    @SuppressLint("NewApi")
    @Override
    public Object invoke(final Object proxy,
                         final Method method,
                         final Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        if (method.isDefault()) {
            throw new UnsupportedOperationException("Default method are not supported");
        }

        final String key = mKeyProcessor.getValue(method, args);
        final Object value = mValueProcessor.getValue(method, args);
        final Class<?> returnType = method.getReturnType();

        return mProvider.getValue(returnType, key, value);
    }
}
