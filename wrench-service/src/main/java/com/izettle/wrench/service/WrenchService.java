package com.izettle.wrench.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.izettle.wrench.service.processor.KeyProcessorFactory;
import com.izettle.wrench.service.processor.ValueProcessorFactory;
import com.izettle.wrench.service.storage.StorageFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public final class WrenchService implements InvocationHandler {

    public static WrenchServiceFactory with(final Context context) {
        final Storage storage = new StorageFactory(context).create();
        final AnnotationProcessor<String> keyProcessor = new KeyProcessorFactory().create();
        final AnnotationProcessor<Object> valProcessor = new ValueProcessorFactory().create();
        return new WrenchServiceFactory(storage, keyProcessor, valProcessor);
    }

    private final Storage mStorage;
    private final AnnotationProcessor<String> mKeyProcessor;
    private final AnnotationProcessor<Object> mValueProcessor;

    @VisibleForTesting
    WrenchService(final Storage storage,
                  final AnnotationProcessor<String> keyProcessor,
                  final AnnotationProcessor<Object> valueProcessor) {
        mStorage = storage;
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

        return mStorage.getValue(returnType, key, value);
    }
}
