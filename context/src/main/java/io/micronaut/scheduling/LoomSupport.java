/*
 * Copyright 2003-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.scheduling;

import io.micronaut.core.annotation.Internal;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Internal
public final class LoomSupport {
    private static final boolean supported;
    private static Throwable failure;

    private static final MethodHandle MH_newThreadPerTaskExecutor;
    private static final MethodHandle MH_ofVirtual;
    private static final MethodHandle MH_name;
    private static final MethodHandle MH_factory;

    static {
        boolean sup;
        MethodHandle newThreadPerTaskExecutor;
        MethodHandle ofVirtual;
        MethodHandle name;
        MethodHandle factory;
        try {
            newThreadPerTaskExecutor = MethodHandles.lookup()
                .findStatic(Executors.class, "newThreadPerTaskExecutor", MethodType.methodType(ExecutorService.class, ThreadFactory.class));
            Class<?> builderCl = Class.forName("java.lang.Thread$Builder");
            Class<?> ofVirtualCl = Class.forName("java.lang.Thread$Builder$OfVirtual");
            ofVirtual = MethodHandles.lookup()
                .findStatic(Thread.class, "ofVirtual", MethodType.methodType(ofVirtualCl));
            name = MethodHandles.lookup()
                .findVirtual(builderCl, "name", MethodType.methodType(builderCl, String.class, long.class));
            factory = MethodHandles.lookup()
                .findVirtual(builderCl, "factory", MethodType.methodType(ThreadFactory.class));

            // invoke, this will throw an UnsupportedOperationException if we don't have --enable-preview
            ofVirtual.invoke();

            sup = true;
        } catch (Throwable e) {
            newThreadPerTaskExecutor = null;
            ofVirtual = null;
            name = null;
            factory = null;
            sup = false;
            failure = e;
        }

        supported = sup;
        MH_newThreadPerTaskExecutor = newThreadPerTaskExecutor;
        MH_ofVirtual = ofVirtual;
        MH_name = name;
        MH_factory = factory;
    }

    private LoomSupport() {}

    public static boolean isSupported() {
        return supported;
    }

    public static void checkSupported() {
        if (!isSupported()) {
            throw new UnsupportedOperationException("Virtual threads are not supported on this JVM, you may have to pass --enable-preview", failure);
        }
    }

    public static ExecutorService newThreadPerTaskExecutor(ThreadFactory threadFactory) {
        checkSupported();
        try {
            return (ExecutorService) MH_newThreadPerTaskExecutor.invokeExact(threadFactory);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ThreadFactory newVirtualThreadFactory(String namePrefix) {
        checkSupported();
        try {
            Object builder = MH_ofVirtual.invoke();
            builder = MH_name.invoke(builder, namePrefix, 1L);
            return (ThreadFactory) MH_factory.invoke(builder);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
