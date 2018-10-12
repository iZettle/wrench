package com.izettle.wrench.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import androidx.annotation.VisibleForTesting;

public final class WrenchService {

    private final Provider mProvider;
    private final AnnotationProcessor<String> mKeyProcessor;
    private final AnnotationProcessor<Object> mValueProcessor;

    @VisibleForTesting
    WrenchService(final Provider storage,
                  final AnnotationProcessor<String> keyProcessor,
                  final AnnotationProcessor<Object> valueProcessor) {
        mProvider = storage;
        mKeyProcessor = keyProcessor;
        mValueProcessor = valueProcessor;
    }

    public static WrenchService with(final Provider provider) {
        final AnnotationProcessor<String> keyProcessor = new KeyProcessor();
        final AnnotationProcessor<Object> valProcessor = new ValueProcessor();
        return new WrenchService(provider, keyProcessor, valProcessor);
    }

    public <T> T create(Class<T> service) {
        final ClassLoader loader = service.getClassLoader();
        final Class[] interfaces = new Class[]{service};

        final InvocationHandler handler = new WrenchServiceHandler(
                mProvider,
                mKeyProcessor,
                mValueProcessor);

        //noinspection unchecked
        return (T) Proxy.newProxyInstance(loader, interfaces, handler);
    }
}
