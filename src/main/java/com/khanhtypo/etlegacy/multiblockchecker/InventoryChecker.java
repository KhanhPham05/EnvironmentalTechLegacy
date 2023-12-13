package com.khanhtypo.etlegacy.multiblockchecker;

import com.khanhtypo.etlegacy.ETLegacy;
import com.khanhtypo.etlegacy.data.TextConstants;
import jakarta.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InventoryChecker {
    private final Map<Block, Integer> structureIngredient;

    InventoryChecker() {
        this.structureIngredient = new HashMap<>();
    }

    void addRequirement(Block block) {
        this.structureIngredient.compute(block, (b, amount) -> amount != null ? amount + 1 : 1);
    }

    Optional<List<Component>> checkInventory(Inventory inventory, @Nullable Map<Block, Integer> excludeMap) {
        Map<Block, Integer> toCheckIngredients = new HashMap<>(this.structureIngredient);
        inventory.items.stream()
                .filter(itemStack -> {
                    Item item = itemStack.getItem();
                    return item instanceof BlockItem && BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(ETLegacy.MOD_ID);
                })
                .forEach(itemStack -> {
                    BlockItem blockItem = ((BlockItem) itemStack.getItem());
                    int itemCount = itemStack.getCount();
                    toCheckIngredients.computeIfPresent(blockItem.getBlock(), (block, amount) -> amount - itemCount <= 0 ? null : amount - itemCount);
                });
        if (excludeMap != null) {
            excludeMap.forEach((block, excludeAmount) -> {
                toCheckIngredients.computeIfPresent(block, (b, remainingAmount) -> remainingAmount - excludeAmount <= 0 ? null : remainingAmount - excludeAmount);
            });
        }

        //if the copied map is empty
        return toCheckIngredients.isEmpty() ? Optional.empty() : Optional.of(this.createInventoryNotEnoughError(toCheckIngredients));
    }

    private List<Component> createInventoryNotEnoughError(Map<Block, Integer> copiedMap) {
        int numberOfBlocksNeeded = copiedMap.size();
        List<Component> components = NonNullList.withSize(numberOfBlocksNeeded + 1, Component.empty());
        components.set(0, TextConstants.INVENTORY_NOT_ENOUGH);
        int i = 1;
        for (Map.Entry<Block, Integer> entry : copiedMap.entrySet()) {
            components.set(i, TextConstants.copyWithArgs(TextConstants.NEED_X_BLOCKS, entry.getValue(), entry.getKey().getName()));
            i++;
        }
        return components;
    }
}