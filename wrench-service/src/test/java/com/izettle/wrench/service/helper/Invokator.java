package com.izettle.wrench.service.helper;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

public class Invokator {

    private Invokator() {
    }

    public static <T> Invokation invoke(final Class<T> clazz, final Invoker<T> invoker) {
        final AtomicReference<Invokation> invokation = new AtomicReference<>();
        final T object = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (o, method, args) -> {
            invokation.set(new Invokation(method, args));
            final Class returnType = method.getReturnType();
            if (returnType.isPrimitive()) {
                if (returnType == Integer.TYPE) {
                    return 0;
                }

                if (returnType == Float.TYPE) {
                    return 0;
                }

                if (returnType == Double.TYPE) {
                    return 0;
                }

                if (returnType == Long.TYPE) {
                    return 0;
                }

                if (returnType == Boolean.TYPE) {
                    return false;
                }
            }

            return null;
        });

        invoker.onInvoke(object);
        return invokation.get();
    }

    public interface Invoker<T> {
        void onInvoke(T object);
    }

    public static class Invokation {
        public final Method method;
        public final Object[] args;

        private Invokation(Method method, Object[] args) {
            this.method = method;
            this.args = args;
        }
    }
}
