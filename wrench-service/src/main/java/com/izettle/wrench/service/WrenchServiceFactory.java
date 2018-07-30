package com.izettle.wrench.service;

import android.support.annotation.VisibleForTesting;

import java.lang.reflect.Proxy;

public final class WrenchServiceFactory {

    private final Storage mStorage;
    private final AnnotationProcessor<String> mKeyProcessor;
    private final AnnotationProcessor<Object> mValueProcessor;

    @VisibleForTesting
    WrenchServiceFactory(final Storage storage,
                         final AnnotationProcessor<String> keyProcessor,
                         final AnnotationProcessor<Object> valueProcessor) {
        mStorage = storage;
        mKeyProcessor = keyProcessor;
        mValueProcessor = valueProcessor;
    }

    public <T> T create(Class<T> service) {
        final ClassLoader loader = service.getClassLoader();
        final Class[] interfaces = new Class[]{service};

        final WrenchService wrenchService = new WrenchService(
                mStorage,
                mKeyProcessor,
                mValueProcessor);

        return (T) Proxy.newProxyInstance(loader, interfaces, wrenchService);
    }
}
