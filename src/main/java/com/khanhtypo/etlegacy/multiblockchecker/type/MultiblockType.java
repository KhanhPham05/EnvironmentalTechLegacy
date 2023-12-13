package com.khanhtypo.etlegacy.multiblockchecker.type;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;

public enum MultiblockType {
    SOLAR_ARRAY;
    private final Set<Block> controllerBlocks;

    MultiblockType() {
        this.controllerBlocks = new HashSet<>();
    }

    public void addBlock(Block block) {
        Preconditions.checkState(
                this.controllerBlocks.add(block),
                "Block %s has already been added to %s".formatted(BuiltInRegistries.BLOCK.getKey(block), toString())
        );
    }

    public Set<Block> getBlocksForType() {
        return ImmutableSet.copyOf(this.controllerBlocks);
    }
}
