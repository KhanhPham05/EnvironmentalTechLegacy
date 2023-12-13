package com.khanhtypo.etlegacy.registering;

import com.google.common.base.Preconditions;

import java.util.function.Supplier;

@Deprecated
public class DeferredObject<T> implements Supplier<T> {
    private final Supplier<T> constructor;
    private T initialized;

    public DeferredObject(Supplier<T> constructor) {
        this.constructor = constructor;
    }


    public T initObject() {
        Preconditions.checkState(this.initialized == null, "Object has already been initialized.");
        return this.initialized = this.constructor.get();
    }

    public T get() {
        Preconditions.checkNotNull(this.initialized, "Object is not yet initialized.");
        return this.initialized;
    }
}
