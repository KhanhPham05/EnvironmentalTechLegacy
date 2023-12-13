package com.khanhtypo.etlegacy.registering;

import com.google.common.collect.Maps;
import com.khanhtypo.etlegacy.registration.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class Register<T> {
    private final Map<T, Holder.Reference<? extends T>> entryMap;
    private final Registry<T> registry;

    public Register(Registry<T> registry) {
        this.registry = registry;
        this.entryMap = new HashMap<>();
    }

    public <A extends T> A register(String name, A object) {
        Registry.register(this.registry, ModItems.id(name), object);
        return object;
    }

    public <A extends T> Holder.Reference<A> registerForHolder(String name, A object) {
        return (Holder.Reference<A>) Registry.registerForHolder(this.registry, ModItems.id(name), object);
    }

    public Map<T, ResourceLocation> getAll() {
        return Maps.asMap(this.entryMap.keySet(), block -> this.entryMap.get(block).key().location());
    }
}
