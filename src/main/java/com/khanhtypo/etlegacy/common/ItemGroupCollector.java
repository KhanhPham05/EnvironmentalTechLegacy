package com.khanhtypo.etlegacy.common;

import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class ItemGroupCollector implements CreativeModeTab.DisplayItemsGenerator {
    private final Set<Holder.Reference<? extends ItemLike>> itemList = new TreeSet<>(Comparator.comparing(holder -> holder.key().location()));

    @Override
    public void accept(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.acceptAll(this.itemList.stream().map(Holder.Reference::value).map(ItemStack::new).toList());
    }

    public void add(Holder.Reference<? extends ItemLike> object) {
        this.itemList.add(object);
    }
}
