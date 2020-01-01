package com.taoing.licensingservice.hystrix;

import com.taoing.licensingservice.utils.UserContext;
import com.taoing.licensingservice.utils.UserContextHolder;

import java.util.concurrent.Callable;

public class DelegatingUserContextCallable<V> implements Callable<V> {
    private final Callable<V> delegate;
    private UserContext originalUserContext;

    public DelegatingUserContextCallable(Callable<V> delegate, UserContext userContext) {
        this.delegate = delegate;
        this.originalUserContext = userContext;
    }

    public V call() throws Exception {
        UserContextHolder.setContext(originalUserContext);

        try {
            return delegate.call();
        }
        finally {
            this.originalUserContext = null;
        }
    }

    public static <V> Callable<V> create(Callable<V> delete, UserContext userContext) {
        return new DelegatingUserContextCallable<>(delete, userContext);
    }
}
