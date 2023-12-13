package com.khanhtypo.etlegacy.registering;

import jakarta.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@Deprecated
public class RegisteredObject<T> implements Supplier<T>, Comparable<ResourceLocation> {
    private final ResourceLocation id;
    private final T object;

    RegisteredObject(ResourceLocation resourceLocation, T object) {
        this.object = object;
        this.id = resourceLocation;
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public T get() {
        return this.object;
    }

    @Override
    public int compareTo(@Nonnull ResourceLocation other) {
        return this.id.compareTo(other);
    }
}
